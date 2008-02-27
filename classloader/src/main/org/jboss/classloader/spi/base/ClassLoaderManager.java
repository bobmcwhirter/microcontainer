/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.classloader.spi.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.jboss.classloader.spi.Loader;
import org.jboss.classloader.spi.base.ClassLoadingTask.ThreadTask;
import org.jboss.logging.Logger;

/**
 * ClassLoaderManager.
 * 
 * @author Scott.Stark@jboss.org
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderManager
{
   /** The log */
   private static Logger log = Logger.getLogger("org.jboss.detailed.classloader.ClassLoaderManager");

   /** The maximum number of CCEs */
   private static final int MAX_CCE = 10;
   
   /** The threads owning the classloader lock */
   private static Map<BaseClassLoader, Thread> loadClassThreads = new HashMap<BaseClassLoader, Thread>();
   
   /** The classloading tasks by thread */
   private static Map<Thread, List<ThreadTask>> loadTasksByThread = Collections.synchronizedMap(new WeakHashMap<Thread, List<ThreadTask>>());

   /** 
    * Register a thread as owning the classloader
    * 
    * @param classloader the classloader
    * @param thread the thread
    * @throws IllegalArgumentException for a null parameter
    */
   static void registerLoaderThread(BaseClassLoader classloader, Thread thread)
   {
      boolean trace = log.isTraceEnabled();
      
      synchronized (loadClassThreads)
      {
         Object previousThread = loadClassThreads.put(classloader, thread);
         if (trace)
            log.trace("registerLoaderThread, classloader=" + classloader + " thread=" + thread + " previousThread=" + previousThread);

         synchronized (loadTasksByThread)
         {
            List<ThreadTask> taskList = loadTasksByThread.get(thread);
            if (taskList == null)
            {
               taskList = Collections.synchronizedList(new LinkedList<ThreadTask>());
               loadTasksByThread.put(thread, taskList);
               if (trace)
                  log.trace("Created new task list for " + thread);
            }
         }
         loadClassThreads.notifyAll();
      }
   }

   /** 
    * Unregister ourselves as owning the classloader lock
    * 
    * @param classLoader the classloader
    * @param thread the thread owning the classloader
    */
   public static void unregisterLoaderThread(BaseClassLoader classLoader, Thread thread)
   {
      boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("unregisterLoaderThread, classloader=" + classLoader + " thread=" + thread);

      // Unregister as the owning thread and notify any waiting threads
      synchronized (loadClassThreads)
      {
         loadClassThreads.remove(classLoader);
         loadClassThreads.notifyAll();
      }

      // Any ThreadTasks associated with this thread must be reassigned
      List<ThreadTask> taskList = loadTasksByThread.get(thread);
      if (taskList != null)
      {
         synchronized (taskList)
         {
            while (taskList.isEmpty() == false)
            {
               ThreadTask threadTask = taskList.remove(0);
               ClassLoadingTask loadTask = threadTask.getLoadTask();
               Thread requestingThread = loadTask.getRequestingThread();
               if( trace )
                  log.trace("Reassigning task: " + threadTask+" to " + requestingThread);
               threadTask.setThread(null);
               // Insert the task into the front of requestingThread task list
               List<ThreadTask> toTaskList = loadTasksByThread.get(requestingThread);
               synchronized (toTaskList)
               {
                  toTaskList.add(0, threadTask);
                  loadTask.nextEvent();
                  toTaskList.notify();
               }
            }
         }
      }
   }

   /**
    * Process the classloading task
    * 
    * @param thread the thread
    * @param task the task
    * @return the loaded class
    * @throws ClassNotFoundException for load exception
    */
   static Class<?> process(Thread thread, ClassLoadingTask task) throws ClassNotFoundException
   {
      while (task.getThreadTaskCount() != 0)
      {
         try
         {
            nextTask(thread, task);
         }
         catch(InterruptedException e)
         {
            task.setLoadError(e);
            break;
         }
      }

      Class<?> loadedClass = task.getLoadedClass();
      if (loadedClass == null)
      {
         Throwable loadException = task.getLoadException();
         if (loadException instanceof ClassNotFoundException)
            throw (ClassNotFoundException) loadException;
         else if (loadException instanceof NoClassDefFoundError)
            throw (NoClassDefFoundError) loadException;
         else if (loadException != null)
         {
            log.warn("Unexpected error during load of:" + task.getClassName(), loadException);
            String msg = "Unexpected error during load of: "+ task.getClassName() + ", msg=" + loadException.getMessage();
            throw new ClassNotFoundException(msg, loadException);
         }
         // Assert that loadedClass is not null
         else
         {
            throw new ClassNotFoundException("Failed to load class " + task.getClassName());
         }
      }
      return loadedClass;
   }

   /**
    * Process the next task
    * 
    * @param thread the thread
    * @param task the task
    * @throws InterruptedException if it is interrupted
    */
   private static void nextTask(Thread thread, ClassLoadingTask task) throws InterruptedException
   {
      boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("Next task thread=" + thread + " task=" + task);
      
      List<ThreadTask> taskList = loadTasksByThread.get(thread);
      synchronized (taskList)
      {
         // There may not be any ThreadTasks
         while (taskList.isEmpty() && task.getThreadTaskCount() != 0 )
         {
            /* There are no more tasks for the calling thread to execute, so the
            calling thread must wait until the task.threadTaskCount reaches 0
             */
            if (trace)
               log.trace("Begin nextTask(WAIT_ON_EVENT), task="+task);
            try
            {
               task.waitOnEvent();
               taskList.wait();
            }
            catch(InterruptedException e)
            {
               if( trace )
                  log.trace("nextTask(WAIT_ON_EVENT), interrupted, task="+task, e);
               // Abort this task t
               throw e;
            }
            if (trace)
               log.trace("nextTask(WAIT_ON_EVENT), notified, task="+task);
         }

         if (trace)
            log.trace("Continue nextTask(" + taskList.size()+"), task="+task);

         // See if the task is complete
         if (task.getThreadTaskCount() == 0)
         {
            task.finish();
            log.trace("End nextTask(FINISHED), task="+task);
            return;
         }
      }

      ThreadTask threadTask = taskList.remove(0);
      ClassLoadingTask loadTask = threadTask.getLoadTask();
      if (trace)
         log.trace("Begin nextTask(" + taskList.size() + "), loadTask=" + loadTask);

      try
      {
         Thread taskThread = threadTask.getThread();
         if (taskThread == null)
         {
            /* This is a task that has been reassigned back to the original
            requesting thread ClassLoadingTask, so a new ThreadTask must
            be scheduled.
            */
            if (trace)
               log.trace("Rescheduling threadTask=" + threadTask);
            scheduleTask(loadTask, threadTask.getLoader(), true);
         }
         else
         {
            if (trace)
               log.trace("Running threadTask=" + threadTask);
            threadTask.run();
         }
      }
      catch (Throwable e)
      {
         if (trace)
            log.trace("Run failed with exception", e);
         boolean retry = e instanceof ClassCircularityError || e.getClass().equals(LinkageError.class);
         if (retry && loadTask.incrementNumCCE() < MAX_CCE)
         {
            /* Reschedule this task after all existing tasks to allow the
            current load tasks which are conflicting to complete.
            */
            try
            {
               // Reschedule and update the loadTask.threadTaskCount
               scheduleTask(loadTask, threadTask.getLoader(), true);
            }
            catch (Throwable ex)
            {
               loadTask.setLoadError(ex);
               log.warn("Failed to reschedule task after CCE", ex);
            }
            if (trace)
               log.trace("Post CCE state, loadTask=" + loadTask);
         }
         else
         {
            loadTask.setLoadError(e);
         }
      }
      finally
      {
         // Release any lock on the classloader
         if (threadTask.isReleaseInNextTask())
            threadTask.getClassLoader().unlock();
      }

      // If the ThreadTasks are complete mark the ClassLoadingTask finished
      if (loadTask.getThreadTaskCount() == 0)
      {
         List<ThreadTask> loadTaskThreadTasks = loadTasksByThread.get(loadTask.getRequestingThread());
         synchronized (loadTaskThreadTasks)
         {
            if( trace )
               log.trace("Notifying task of thread completion, loadTask:"+loadTask);
            task.finish();
            loadTaskThreadTasks.notify();
         }
      }
      if (trace)
         log.trace("End nextTask(" + taskList.size()+ "), loadTask=" + loadTask);
   }

   /** 
    * Invoked to create a ThreadTask to assign a thread to the task of
    * loading the class of ClassLoadingTask.
    *
    * @param task the classloading task
    * @param loader the loader
    * @param reschedule a boolean indicating if this task is being rescheduled with another classloader
    */
   static void scheduleTask(ClassLoadingTask task, Loader loader, boolean reschedule)
   {
      boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("ScheduleTask task=" + task + " loader=" + loader + " reschedule=" + reschedule);
      
      Thread thread;
      boolean releaseInNextTask = false;
      ThreadTask subtask;
      List<ThreadTask> taskList;

      BaseClassLoader classLoader = null;
      if (loader instanceof BaseDelegateLoader)
      {
         BaseDelegateLoader delegateLoader = (BaseDelegateLoader) loader;
         BaseClassLoaderPolicy policy = delegateLoader.getPolicy();
         if (policy == null)
            throw new IllegalStateException("Null policy for " + delegateLoader);
         classLoader = policy.getClassLoader();
      }
      
      synchronized (loadClassThreads)
      {
         // Find the thread that owns the classloader
         if (classLoader == null)
         {
            thread = task.getRequestingThread();
            synchronized (loadTasksByThread)
            {
               List<ThreadTask> list = loadTasksByThread.get(thread);
               if (list == null)
               {
                  list = Collections.synchronizedList(new LinkedList<ThreadTask>());
                  loadTasksByThread.put(thread, list);
                  if (trace)
                     log.trace("Created new task list for " + thread);
               }
            }
         }
         else
         {
            thread = loadClassThreads.get(classLoader);
         }
         
         if (thread == null)
         {
            boolean interrupted = Thread.interrupted();
            int waits = 0;
            
            try
            {
               // No thread, try to get the lock ourselves
               while (thread == null)
               {
                  try
                  {
                     boolean gotLock = classLoader.attemptLock();
                     if (gotLock == false)
                     {
                        // Two minutes should be long enough?
                        if (waits++ == 12)
                           throw new IllegalStateException("Waiting too long to get the registration lock for classLoader " + classLoader);
                        // Wait 10 seconds
                        if (trace)
                           log.trace(classLoader + " waiting for lock");
                        loadClassThreads.wait(10000);
                     }
                     else
                     {
                        releaseInNextTask = true;
                     }
                  }
                  catch (InterruptedException ignored)
                  {
                  }
                  thread = loadClassThreads.get(classLoader);
               }
            }
            finally
            {
               if (interrupted)
                  Thread.currentThread().interrupt();
            }
         }

         // Now that we have the owner thread, create and assign the task
         subtask = task.newThreadTask(loader, thread, reschedule, releaseInNextTask);
         // Add the task to the owning thread
         taskList = loadTasksByThread.get(thread);
         synchronized (taskList)
         {
            taskList.add(subtask);
            taskList.notify();
            if (trace)
               log.trace("scheduleTask(" + taskList.size() + "), created subtask: " + subtask);
         }
      }
   }
}
