package bot.engTrainer.scenariodefine.simplescenario;

import bot.engTrainer.scenariodefine.Scenario;
import bot.engTrainer.scenariodefine.ScenarioStage;
import com.pengrad.telegrambot.model.Chat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса Scenario
 * @param <T> - тип используемый для идентификации состояний сценария
 * @param <P> - тип описывающий параметры этапов сценария
 */
@Getter
@Setter
public abstract class SimpleScenario<T, P> implements Scenario<T, P> {

    String scenarioId;

    private List<ScenarioStage<T, P>> stageChain;
    private ScenarioStage<T, P> currentStage;
    private boolean done;
    private boolean started;

    public SimpleScenario() {
        this.stageChain = new ArrayList<>();
        init();
    }

    /**
     * Инициализация сценария. Возможно определение этапов сценария
     */
    public abstract void init();
    /**
     * Завершение сценария. Возможно выполнение действий по результатам работы сценария
     */
    public abstract void finish();

    @Override
    public void goToEnd() {
        this.currentStage = null;
        this.done = true;
    }

    @Override
    public void goToStart() {
        start();
    }

    @Override
    public void doWork(P stageParams) {
        Optional<T> nextStage = Optional.empty();
        if(currentStage!=null) {
            nextStage = currentStage.getNextStage(stageParams);
        }
        nextStage.ifPresentOrElse(
                (v)-> {
                    if (!v.equals(currentStage.getIdentifier())) {
                        goToStage(v);
                        checkEndState();
                    }
                },
                this::goToEnd
        );
    }

    public void resume(Object param){

    }

    @Override
    public void start() {
        this.started = true;
        if (stageChain.size()>0) {
            this.currentStage = stageChain.get(0);
        }
        checkEndState();
    }

    @Override
    public ScenarioStage<T, P> getCurrentStage() {
        return currentStage;
    }

    @Override
    public void goToStage(T stageId) {
        getStage(stageId).ifPresent((v)-> {
                                            this.currentStage = v;
                                            checkEndState();
        });
    }

    @Override
    public void addStage(ScenarioStage<T, P> scenarioStage) {
        stageChain.add(scenarioStage);
    }

    @Override
    public void setScenarioStage(T identifier, ScenarioStage<T, P> scenarioStage) {

    }

    @Override
    public void delScenarioStage(T identifier) {

    }

    @Override
    public List<ScenarioStage<T, P>> getStages() {
        return Collections.unmodifiableList(stageChain);
    }

    @Override
    public Optional<ScenarioStage<T, P>> getStage(T stageId) {
        Optional<ScenarioStage<T, P>> result = Optional.empty();
        for (ScenarioStage<T, P> stage: stageChain) {
            if(stage.getIdentifier().equals(stageId)){
                result  = Optional.of(stage);
            }
        }
        return result;
    }

    @Override
    public String getId() {
        return scenarioId;
    }

    @Override
    public void setId(String id) {
        this.scenarioId = id;
    }

    @Override
    public long save() {
        return 0;
    }

    @Override
    public void load(long id) {

    }

    protected void checkEndState(){
        if (stageChain.size()>0) {
            if (this.currentStage == null){
                this.done = true;
            }
        }else{
            this.done = true;
        }
    }

}
