package bot.engTrainer.scenariodefine;

import java.util.List;
import java.util.Optional;

/**
 * Описание сценария
 * T - тип описывающий состояния сценария
 * P - тип описывающий параметры этапов сценария
 */
public interface Scenario<T, P> {
    /**
     * Выполнение работы в текущем этапе сценария
     * @param params - объект содержащий параметры для выполнения этапа
     */
    void doWork(P params);

    /**
     * Старт сценария. Выполнение действий по подготовке к выполнению.
     */
    void start();

    /**
     * Получение текущего этапа сценария
     * @return - текущий этап
     */
    ScenarioStage<T, P> getCurrentStage();

    /**
     * Переход к этапу с указанным идентификатором
     * @param stageId - идентификатор этапа для перехода
     */
    void goToStage(T stageId);

    /**
     * Завершение сценария
     */
    void goToEnd();

    /**
     * Продолжение поле остановки
     */
    void resume(Object params);

    /**
     * Переход к точке старта сценария.
     */
    void goToStart();

    /**
     * Добавление нового этапа в сценарий
     * @param scenarioStage - объект этапа сценария
     */
    void addStage(ScenarioStage<T, P> scenarioStage);

    /**
     * Замена этапа сценария с указанным идентификатором
     * @param identifier - идентификатор заменяемого сценария
     * @param scenarioStage - этап сценария
     */
    void setScenarioStage(T identifier, ScenarioStage<T, P> scenarioStage);

    /**
     * Удаление этапа с указанным идентификтаором
     * @param identifier - идентификатор удаляемого сценария
     */
    void delScenarioStage(T identifier);

    /**
     * Получение всех этапов сценария
     * @return список с этапами
     */
    List<ScenarioStage<T, P>> getStages();

    /**
     * Получение этапа по идентификатору
     * @param stageId - идентификатор этапа
     * @return найденный по идентификатору эатп, если такого этапа нет, то пустое значение
     */
    Optional<ScenarioStage<T, P>> getStage(T stageId);

    /**
     * Сохранение данных сценария
     * @return id сохраненной записи
     */
    long save();

    /**
     * Загрузка данных сценария
     * @param id - идентификатор записи по которому будет выполнен поиск сохраненного сценария
     */
    void load(long id);

    /**
     * Получение идентификатора сценария
     * @return - идентификатор сценария
     */
    String getId();

    /**
     * Установка идентификатора сценария
     * @param id - новый идентификатор
     */
    void setId(String id);
}
