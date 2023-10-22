package com.haw.se1lab.facade.impl;

import com.haw.se1lab.common.api.datatype.TaskId;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.Memo;
import com.haw.se1lab.dataaccess.api.entity.Task;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.dataaccess.api.repo.MemoRepository;
import com.haw.se1lab.facade.api.TaskFacade;
import com.haw.se1lab.logic.api.usecase.TaskUseCase;
import com.haw.se1lab.logic.api.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Facade Implementierung für Task
 *
 * @author Kjell May
 */
@RestController
@RequestMapping(path = "/tasks")
public class TaskFacadeImpl implements TaskFacade {

    @Autowired
    private TaskUseCase taskUseCase;

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private MemoRepository memoRepository;


    /**
     * Returns all available tasks.
     *
     * @return the found task or an empty list if none were found
     */
    @GetMapping
    @Override
    public List<Task> getTasks() {
        return taskUseCase.findAllTasks();
    }

    //TODO mapping
    @Override
    public List<Task> getTasksBySubjectId(long id) throws SubjectNotFoundException {
        return taskUseCase.findTasksBySubjectId(id);
    }

    /**
     * Returns the task with the given ID.
     *
     * @param id the task's id
     * @return the found task
     * @throws TaskNotFoundException in case the task could not be found
     */
    @GetMapping(value = "/{id:\\d+}")
    @Override
    public Task getTaskById(@PathVariable("id") long id) throws TaskNotFoundException {
        return taskUseCase.findTaskById(id);
    }

    /**
     * Returns the task with the given ID.
     *
     * @param taskId the task's id
     * @return the found task
     * @throws TaskNotFoundException in case the task could not be found
     */
    @GetMapping(value = "/taskId/{taskId:\\w{2}\\d{3}}")
    @Override
    public Task getTaskByTaskId(@PathVariable("taskId") TaskId taskId) throws TaskNotFoundException {
        return taskUseCase.findTaskByTaskId(taskId);
    }

    /**
     * Creates a task with the given data.
     *
     * @param task the task to be created; must not be <code>null</code>
     * @return the created task
     * @throws TaskAlreadyExistingException in case a task with the given data already exists
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Override
    public Task createTask(@RequestBody Task task) throws TaskAlreadyExistingException, MemoNotFoundException {
        Assert.notNull(task, "Task darf nicht null sein");

        Memo memo = memoRepository.findById(task.getMemo().getId()).orElseThrow(() -> new MemoNotFoundException(task.getMemo().getId()));
        return taskUseCase.createTask(memo, task.getTitle(), task.getTaskId(), task.getRewardXP(), task.getType(), task.getContent());
    }

    /**
     * Updates a newTask with the given data.
     *
     * @param newTask the newTask to be updated; must not be <code>null</code>
     * @return the updated newTask
     * @throws TaskNotFoundException in case the newTask could not be found
     */
    @PutMapping("/{id:\\d+}")
    @Transactional
    @Override
    public Task updateTask(@PathVariable("id") long id, @RequestBody Task newTask) throws TaskNotFoundException {
        Task currentTask = taskUseCase.findTaskById(id);
        return taskUseCase.updateTask(currentTask, newTask);
    }

    /**
     * Deletes the task with the given ID.
     *
     * @param id the task's technical ID
     * @throws TaskNotFoundException in case the task could not be found
     */
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Override
    public void deleteTask(@PathVariable("id") long id) throws TaskNotFoundException {
        taskUseCase.deleteTask(id);
    }

    @PutMapping(value = "/check/{id:\\d+}")
    @Transactional
    @Override
    public User checkTask(@PathVariable("id") long id) throws TaskNotFoundException, TaskCheckingException, UserNotFoundException, TaskAlreadyInUserException, UserAlreadyInTaskException {
        Assert.notNull(id, "ID darf nicht null sein");

        taskUseCase.checkTask(userUseCase.getCurrentUser(), taskUseCase.findTaskById(id));
        return userUseCase.getCurrentUser();
    }

    @PutMapping(value = "/uncheck/{id:\\d+}")
    @Transactional
    @Override
    public User uncheckTask(@PathVariable("id") long id) throws TaskNotFoundException, TaskCheckingException, UserNotFoundException {
        Assert.notNull(id, "ID darf nicht null sein");
        taskUseCase.uncheckTask(userUseCase.getCurrentUser(), taskUseCase.findTaskById(id));
        return userUseCase.getCurrentUser();
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Task not found.")
    private void handleTaskNotFoundException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

    @ExceptionHandler(TaskAlreadyExistingException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Task already exists.")
    private void handleTaskAlreadyExistingException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }
}
