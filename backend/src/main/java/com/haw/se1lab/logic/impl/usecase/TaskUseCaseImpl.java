package com.haw.se1lab.logic.impl.usecase;

import com.haw.se1lab.common.api.datatype.TaskId;
import com.haw.se1lab.common.api.datatype.TaskType;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.*;
import com.haw.se1lab.dataaccess.api.repo.TaskRepository;
import com.haw.se1lab.logic.api.usecase.SubjectUseCase;
import com.haw.se1lab.logic.api.usecase.TaskUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementierende Use Case Klasse für Task
 *
 * @author Kjell May
 * @author Isabell Schettler
 */
@Service
public class TaskUseCaseImpl implements TaskUseCase {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubjectUseCase subjectUseCase;

    /**
     * Gibt alle Tasks zurück
     *
     * @return List<Task> Eine Liste der Tasks
     */
    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findTasksBySubjectId(long subjectId) throws SubjectNotFoundException {
        Subject subject = subjectUseCase.findSubjectById(subjectId);
        List<Memo> memos = subject.getMemos();
        List<Task> tasks = new ArrayList<>();
        memos.forEach(x -> tasks.addAll(x.getTasks()));

        return tasks;
    }

    /**
     * GIbt eine Task nach ID zurück
     *
     * @param id Die Id der Task
     * @return die gesuchte Task
     * @throws TaskNotFoundException wenn Task nicht gefunden wurde
     */
    @Override
    public Task findTaskById(long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(""));
    }

    /**
     * GIbt eine Task nach ID zurück
     *
     * @param taskId Die Id der Task
     * @return die gesuchte Task
     * @throws TaskNotFoundException wenn Task nicht gefunden wurde
     */
    @Override
    public Task findTaskByTaskId(TaskId taskId) throws TaskNotFoundException {
        return taskRepository.findByTaskId(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    /**
     * Lässt eine Task erstellen
     *
     * @param title    Titel der Task
     * @param taskId       Id der Task
     * @param rewardXP zugehörigen Belohnungspunkte
     * @param type     Task-Typ
     * @param content  Inhalt der Task
     * @return die erstellte Task
     * @throws TaskAlreadyExistingException wenn es bereits eine Task mit dieser Id gibt
     */
    @Override
    public Task createTask(Memo memo, String title, TaskId taskId, int rewardXP, TaskType type, String content) throws TaskAlreadyExistingException {
        Assert.notNull(title, "Titel darf nicht null sein");
        Assert.notNull(taskId, "ID darf nicht null sein");
        Assert.notNull(type, "TaskType darf nicht null sein");
        Assert.notNull(content, "Inhalt darf nicht null sein");

        if (taskRepository.findByTaskId(taskId).isPresent()) throw new TaskAlreadyExistingException(title);

        Task task = new Task(memo, title, taskId, rewardXP, type, content);

        return taskRepository.save(task);
    }

    /**
     * Lässt eine Task erstellen
     *
     * @param currentTask Die zu aktualisierende Task
     * @return die aktualisierte Task
     * @throws TaskNotFoundException Wenn es die Task nicht gibt
     */
    @Override
    public Task updateTask(Task currentTask, Task newTask) throws TaskNotFoundException {
        Assert.notNull(newTask, "Task darf nicht null sein");

        if (newTask.getTaskId() != null && newTask.getTaskId().getTaskId() != null) {
            TaskId newTaskId;
            try {
                newTaskId = new TaskId(newTask.getTaskId().getSubjectAcronym(), newTask.getTaskId().getSubjectNumber(),
                        newTask.getTaskId().getTaskNumber());
            } catch (Exception exception) {
                throw new IllegalArgumentException("Ungültige neue TaskId");
            }
            currentTask.setTaskId(newTaskId);
        }
        if (newTask.getRewardXP() != 0) {
            currentTask.setRewardXP(newTask.getRewardXP());
        }
        if (newTask.getContent() != null) {
            currentTask.setContent(newTask.getContent());
        }
        if (newTask.getTitle() != null) {
            currentTask.setTitle(newTask.getTitle());
        }
        if (newTask.getType() != null) {
            currentTask.setType(newTask.getType());
        }

        return taskRepository.save(currentTask);
    }

    /**
     * Lässt eine Task löschen
     *
     * @param id Id der zu löschenden Task
     * @throws TaskNotFoundException Wenn es die Task nicht gibt
     */
    @Override
    public void deleteTask(long id) throws TaskNotFoundException {
        Task task = findTaskById(id);
        task.getMemo().removeTask(task);
        for (User user: task.getFinishedUsers()) {
            user.removeTask(task);
        }

        taskRepository.delete(task);
    }

    @Override
    public Task checkTask(User user, Task task) throws TaskNotFoundException, TaskCheckingException, TaskAlreadyInUserException, UserAlreadyInTaskException {
        Assert.notNull(task, "Task darf nicht null sein");

        if (user.getFinishedTasks().contains(task)) throw new TaskCheckingException(task, user);
        if (!user.getTasks().contains(task)) throw new TaskNotFoundException(task.getTaskId());
        if (user.getLevelsystem() != null) {
            user.getLevelsystem().increaseXP(task.getRewardXP());
        }
        user.addTask(task);
        task.addUser(user);

        return taskRepository.save(task);
    }

    @Override
    public Task uncheckTask(User user, Task task) throws TaskNotFoundException, TaskCheckingException, UserNotFoundException {
        Assert.notNull(task, "Task darf nicht null sein");

        if (!user.getFinishedTasks().contains(task)) throw new TaskCheckingException(task, user);
        if (!user.getTasks().contains(task)) throw new TaskNotFoundException(task.getTaskId());
        if (user.getLevelsystem() != null) {
            user.getLevelsystem().decreaseXP(task.getRewardXP());
        }
        user.removeTask(task);
        task.removeUser(user);

        return taskRepository.save(task);
    }

}
