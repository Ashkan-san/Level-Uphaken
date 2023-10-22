package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.common.api.datatype.TaskId;
import com.haw.se1lab.common.api.datatype.TaskType;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.Memo;
import com.haw.se1lab.dataaccess.api.entity.Task;
import com.haw.se1lab.dataaccess.api.entity.User;

import java.util.List;

/**
 * Use Case Interface für Task
 *
 * @author Kjell May
 * @author Isabell Schettler
 */
public interface TaskUseCase {

    /**
     * Gibt alle Tasks zurück
     *
     * @return List<Task> Eine Liste der Tasks
     */
    List<Task> findAllTasks();

    /**
     * Gibt alle Tasks von einem subject zurück
     * @param subjectId id des subjets
     * @return List<Task> Eine Liste der Tasks
     */
    List<Task> findTasksBySubjectId(long subjectId) throws SubjectNotFoundException;

    /**
     * GIbt eine Task nach ID zurück
     *
     * @param id Die Id der Task
     * @return die gesuchte Task
     * @throws TaskNotFoundException wenn Task nicht gefunden wurde
     */
    Task findTaskById(long id) throws TaskNotFoundException;

    /**
     * GIbt eine Task nach ID zurück
     *
     * @param taskId Die Id der Task
     * @return die gesuchte Task
     * @throws TaskNotFoundException wenn Task nicht gefunden wurde
     */
    Task findTaskByTaskId(TaskId taskId) throws TaskNotFoundException;

    /**
     * Lässt eine Task erstellen
     *
     * @param title    Titel der Task
     * @param id       Id der Task
     * @param rewardXP zugehörigen Belohnungspunkte
     * @param type     Task-Typ
     * @param content  Inhalt der Task
     * @return die erstellte Task
     * @throws TaskAlreadyExistingException wenn es bereits eine Task mit dieser Id gibt
     */
    Task createTask(Memo memo, String title, TaskId id, int rewardXP, TaskType type, String content) throws TaskAlreadyExistingException;

    /**
     * Lässt eine Task erstellen
     *
     * @param currentTask Die zu aktualisierende Task
     * @return die aktualisierte Task
     * @throws TaskNotFoundException Wenn es die Task nicht gibt
     */
    Task updateTask(Task currentTask, Task newTask) throws TaskNotFoundException;

    /**
     * Lässt eine Task löschen
     *
     * @param id Id der zu löschenden Task
     * @throws TaskNotFoundException Wenn es die Task nicht gibt
     */
    void deleteTask(long id) throws TaskNotFoundException;

    Task checkTask(User user, Task task) throws TaskNotFoundException, TaskCheckingException, TaskAlreadyInUserException, UserAlreadyInTaskException;

    Task uncheckTask(User user, Task task) throws TaskNotFoundException, TaskCheckingException, UserNotFoundException;
}
