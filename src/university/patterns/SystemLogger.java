package university.patterns;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import university.interfaces.Observer;

public class SystemLogger implements Observer, Serializable {
    private final List<String> logs = new ArrayList<>();

    @Override
    public void onEvent(String event) {
        logs.add(LocalDateTime.now() + " | " + event);
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }
}
