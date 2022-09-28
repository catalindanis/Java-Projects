package platform;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.springframework.util.StopWatch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name="Code")
public class Code{
    @Id
    UUID id;
    String code = "";
    final LocalDateTime date = LocalDateTime.now();
    int time;
    int views;
    boolean timeRestriction;
    boolean viewsRestriction;

    public Code(){
        id = UUID.randomUUID();
    }
    public Code(String code,int time,int views){
        this.code = code;
        this.time = time;
        this.views = views;
        id = UUID.randomUUID();
    }

    public void setCode(String code){
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name="time")
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        if(time < 0)
            time = 0;
        this.time = time;
    }

    @Column(name="views")
    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        if(views < 0)
            views = 0;
        this.views = views;
    }

    public void setViews1(int views) {
        this.views = views;
    }

    @Column(name="code")
    public String getCode() {
        return code;
    }

    @Column(name="date")
    public String getDate(){
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public LocalTime getTotalTimeSeconds(){
        return date.toLocalTime().plusSeconds(time);
    }

    public void checkRestrictions(){
        timeRestriction = time > 0 ? true : false;
        viewsRestriction = this.views > 0 ? true : false;
    }

    @Column(name = "timerestriction")
    public boolean isTimeRestriction() {
        return timeRestriction;
    }

    public void setTimeRestriction(boolean timeRestriction) {
        this.timeRestriction = timeRestriction;
    }

    @Column(name = "viewsrestriction")
    public boolean isViewsRestriction() {
        return viewsRestriction;
    }

    public void setViewsRestriction(boolean viewsRestriction) {
        this.viewsRestriction = viewsRestriction;
    }

    public String toString(){
        return String.format("\"Code\":{\n" +
                "\"id\":\"%s\",\n" +
                "\"code\":\"%s\",\n" +
                "\"date\":\"%s\",\n" +
                "\"time\":\"%s\",\n" +
                "\"views\":\"%s\"\n" +
                "}",id,code,date,time,views);
    }
}