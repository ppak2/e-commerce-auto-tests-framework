package browserstack;

import lombok.*;

import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Build {

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private String name;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private String id;

    @Getter
    private String status;

    public Build(Map<String, String> jsonValue){
        this.name = jsonValue.get("name");
        this.id = jsonValue.get("hashed_id");
        this.status = jsonValue.get("status");
    }

    public Build(String buildName, String buildID){
        this.name = buildName;
        this.id = buildID;
    }

}
