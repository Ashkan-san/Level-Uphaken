package com.haw.se1lab.facade.api;

import com.haw.se1lab.common.api.datatype.TaskId;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.Task;
import com.haw.se1lab.dataaccess.api.entity.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Represents a facade for the system operations for tasks which are
 * available from outside the system.
 *
 * @author Isabell Schettler
 */
public interface TaskFacade {
    /**
     * Returns all available tasks.
     *
     * @return the found task or an empty list if none were found
     */
    List<Task> getTasks();

    /**
     * Returns all available tasks.
     *
     * @param id the id from the subject
     * @return the found task or an empty list if none were found
     */
    List<Task> getTasksBySubjectId(long id) throws SubjectNotFoundException;

    /**
     * Returns the task with the given ID.
     *
     * @param id the task's id
     * @return the found task
     * @throws TaskNotFoundException in case the task could not be found
     */
    Task getTaskById(long id) throws TaskNotFoundException;

    /**
     * Returns the task with the given ID.
     *
     * @param id the task's id
     * @return the found task
     * @throws TaskNotFoundException in case the task could not be found
     */
    Task getTaskByTaskId(TaskId id) throws TaskNotFoundException;

    /**
     * Creates a task with the given data.
     *
     * @param task the task to be created; must not be <code>null</code>
     * @return the created task
     * @throws TaskAlreadyExistingException in case a task with the given data already exists
     */
    Task createTask(Task task) throws TaskAlreadyExistingException, MemoNotFoundException;

    /**
     * Updates a newTask with the given data.
     *
     * @param newTask the newTask to be updated; must not be <code>null</code>
     * @return the updated newTask
     * @throws TaskNotFoundException in case the newTask could not be found
     */
    Task updateTask(long id, Task newTask) throws TaskNotFoundException;

    /**
     * Deletes the task with the given ID.
     *
     * @param id the task's id
     * @throws TaskNotFoundException in case the task could not be found
     */
    void deleteTask(long id) throws TaskNotFoundException;

    User checkTask(long id) throws TaskNotFoundException, TaskCheckingException, UserNotFoundException, TaskAlreadyInUserException, UserAlreadyInTaskException;

    User uncheckTask(long id) throws TaskNotFoundException, TaskCheckingException, UserNotFoundException;
}
