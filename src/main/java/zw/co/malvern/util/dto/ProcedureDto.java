package zw.co.malvern.util.dto;

public class ProcedureDto {
    private String routineSchema;
    private String name;
    private String routineBodyDefination;
    private String routineBodyLanguage;
    private String determinstic;
    private String securityType;
    private String comment;
    private String definer;
    private String sqlMode;
    private String dateCreated;
    private String lastAltered;

    public String getRoutineSchema() {
        return routineSchema;
    }

    public void setRoutineSchema(String routineSchema) {
        this.routineSchema = routineSchema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoutineBodyDefination() {
        return routineBodyDefination;
    }

    public void setRoutineBodyDefination(String routineBodyDefination) {
        this.routineBodyDefination = routineBodyDefination;
    }

    public String getRoutineBodyLanguage() {
        return routineBodyLanguage;
    }

    public void setRoutineBodyLanguage(String routineBodyLanguage) {
        this.routineBodyLanguage = routineBodyLanguage;
    }

    public String getDeterminstic() {
        return determinstic;
    }

    public void setDeterminstic(String determinstic) {
        this.determinstic = determinstic;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefiner() {
        return definer;
    }

    public void setDefiner(String definer) {
        this.definer = definer;
    }

    public String getSqlMode() {
        return sqlMode;
    }

    public void setSqlMode(String sqlMode) {
        this.sqlMode = sqlMode;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastAltered() {
        return lastAltered;
    }

    public void setLastAltered(String lastAltered) {
        this.lastAltered = lastAltered;
    }
}
