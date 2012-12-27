package controllers;

import com.github.jmkgreen.morphia.Key;
import com.mongodb.WriteResult;
import models.Note;
import net.onlite.morplay.MorplayPlugin;
import net.onlite.morplay.mongo.MongoCollection;
import org.bson.types.ObjectId;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.List;

import static play.data.Form.form;

/**
 * Notes controller
 */
public class NotesController extends Controller {
    /**
     * Home page
     */
    public static Result index() {
        // Get collection
        MongoCollection<Note> collection = MorplayPlugin.store().collection(Note.class);

        // Get notes
        F.Promise<List<Note>> notes = collection.find()
                                                .order("-date")
                                                .limit(20)
                                                .asList();


        return async(notes.map(new F.Function<List<Note>, Result>() {
            @Override
            public Result apply(List<Note> notes) throws Throwable {
                return ok(index.render(notes, form(Note.class)));
            }
        }));
    }

    /**
     * Add note
     */
    public static Result create() {
        // Bind form
        Form<Note> form = form(Note.class).bindFromRequest(request());

        // Get collection
        MongoCollection<Note> collection = MorplayPlugin.store().collection(Note.class);

        if (form.hasErrors()) {
            flash("error", "Can not create note!");
        } else {
            // Create entity
            F.Promise<Key<Note>> promise = collection.create(form.get());

            return async(promise.map(new F.Function<Key<Note>, Result>() {
                @Override
                public Result apply(Key<Note> noteKey) throws Throwable {
                    // Success message
                    flash("message", "Note added!");

                    return redirect(routes.NotesController.index());
                }
            }));
        }


        return redirect(routes.NotesController.index());
    }

    /**
     * Delete note
     */
    public static Result delete() {
        // Bind form
        DynamicForm form = form().bindFromRequest(request());

        // Get collection
        MongoCollection<Note> collection = MorplayPlugin.store().collection(Note.class);

        if (form.get("id") == null) {
            flash("error", "Can not delete note!");
        } else {
            F.Promise<WriteResult> promise = collection.removeById(new ObjectId(form.get("id")));

            return async(promise.map(new F.Function<WriteResult, Result>() {
                @Override
                public Result apply(WriteResult writeResult) throws Throwable {
                    // Success message
                    flash("message", "Note removed!");

                    return redirect(routes.NotesController.index());
                }
            }));
        }

        return redirect(routes.NotesController.index());
    }
}
