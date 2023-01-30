package bot.engTrainer.scenariodefine;

import java.util.Optional;
import java.util.function.Function;

/**
 * Описание этапа сценария
 * T - тип идентификаторов этапов
 * P - тип описывающий параметры этапов сценария
 */
public interface ScenarioStage<T, P> {

    /**
     * Установка метода выполнения текущего этапа - worker. Метод принимает на вход объект с параметрами (типа P) и должен вернуть идентификатор следующего этапа (значение типа T)
     * Если будет возвращено null, то выполнение сценария завершается.
     * Если возвращено значение отличное от null, то в сценарии выполняется переход к этапу с указанным идентификатором.
     * Может быть возвращен идентификатор текущего этапа, если не выполнены условия перехода к следующему этапу
     * @param worker - функция принимающая параметры на вход и возвращающая
     */
    void setWorker(Function<P, T> worker);

    /**
     * Возвращение идентификатора текущего этапа
     * @return Идетификатор
     */
    T getIdentifier();

    /**
     * Установка идентификатора текущего эатпа
     * @param id - идентификатор
     */
    void setIdentifier(T id);

    /**
     * Метод выполняет функцию текущего этапа - worker
     * @param stageParams - параметры для выполнения функции
     * @return - идентификатор следующего этапа (см. setWorker)
     */
    Optional<T> getNextStage(P stageParams);
}