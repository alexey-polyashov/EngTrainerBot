package bot.engTrainer.scenariodefine;

import java.util.Optional;

/**
 * Менеджер сценариев. Содержит список используемых сценариев. Осуществляет регистрацию и поиск по идентификатору.
 */
public interface ScManager<T, P> {

    /**
     * Получает сценарий по идентификатору
     * @param id - идентификатор сценария
     * @return - новый экземпляр сценария
     */
    Optional<Scenario<T, P>> getScenarioById(String id);

    /**
     * Регистрация класса сценария
     * @param scenario - слкасс сценария
     * @param id - идентификатор
     */
    void attach(Class scenario, String id);

    /**
     * Удаление сценария из списка зарегистрированных
     * @param id - идентификатор сценария
     */
    void detach(String id);
}
