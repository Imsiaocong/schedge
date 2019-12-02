package api;

import io.javalin.Javalin;
import models.Course;
import models.SubjectCode;
import models.Term;
import services.JsonMapper;
import java.util.List;
import services.SelectCourses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  public static void run() {
    Javalin app = Javalin.create().start(80);
    Logger logger = LoggerFactory.getLogger("app");

    app.get("/schools", ctx -> {
      ctx.result(JsonMapper.toJson(SubjectCode.allSchools()));
    });

    app.get("/subjects", ctx -> {
      List<SubjectCode> subjects = SubjectCode.allSubjects();
      ctx.result(JsonMapper.toJson(subjects));
    });

    app.get("/subjects/:school", ctx -> {
      List<SubjectCode> subjects =
          SubjectCode.allSubjects(ctx.pathParam("school"));
      ctx.result(JsonMapper.toJson(subjects));
    });

    app.get("/:term/:school/:subject", ctx -> {
      Term term = Term.fromId(Integer.parseInt(ctx.pathParam("term")));
      SubjectCode subject =
          new SubjectCode(ctx.pathParam("subject"), ctx.pathParam("school"));

      ctx.result(JsonMapper.toJson(
          SelectCourses.selectCourses(logger, term, subject)));
    });
  }
}
