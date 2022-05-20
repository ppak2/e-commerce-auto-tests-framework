package browserstack;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Session {

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private String name;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private String id;

    @Getter
    @Setter
    private String buildName;

    @Getter
    @Setter
    private String projectName;


    public Session(Map<String, String> jsonValue) {
        this.name = jsonValue.get("name");
        this.id = jsonValue.get("hashed_id");
        this.buildName = jsonValue.get("build_name");
        this.projectName = jsonValue.get("project_name");
    }

    public Session(String name, String id){
        this.name = name;
        this.id = id;
    }
}
