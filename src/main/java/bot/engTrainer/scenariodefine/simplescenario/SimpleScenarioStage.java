package bot.engTrainer.scenariodefine.simplescenario;

import bot.engTrainer.scenariodefine.ScenarioStage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class SimpleScenarioStage<T, P> implements ScenarioStage<T, P> {

    Function<P, T> worker;
    T identifier;

    public SimpleScenarioStage(T id, Function<P, T> worker){
        this.identifier = id;
        this.worker = worker;
    }

    @Override
    public void setWorker(Function<P, T> worker) {
        this.worker = worker;
    }

    @Override
    public Optional<T> getNextStage(P stageParams) {
        Optional<T> result = Optional.empty();
        T stageId = worker.apply(stageParams);
        if(stageId!=null){
            result = Optional.of(stageId);
        }
        return result;
    }

    @Override
    public void setIdentifier(T id) {
        this.identifier = id;
    }

    @Override
    public T getIdentifier() {
        return identifier;
    }

}
