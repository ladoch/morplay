package controllers;

import models.Note;
import net.onlite.morplay.MorplayPlugin;
import net.onlite.morplay.mongo.AtomicOperation;
import net.onlite.morplay.mongo.Filter;
import net.onlite.morplay.mongo.MongoCollection;
import org.bson.types.ObjectId;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.List;
import java.util.Map;

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
        List<Note> notes = collection.query()
                                     .order("-date")
                                     .limit(20)
                                     .asList();


        return ok(index.render(notes, form(Note.class)));
    }

    /**
     * Add note
     */
    public static Result create() {
        // Bind form
        Form<Note> form = form(Note.class).bindFromRequest(request());

        if (form.hasErrors()) {
            flash("error", "Can not create note!");
        } else {
            // Get collection
            MongoCollection<Note> collection = MorplayPlugin.store().collection(Note.class);

            // Create entity
            collection.create(form.get());

            // Success message
            flash("message", "Note added!");
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
            collection.remove(new Filter("_id", new ObjectId(form.get("id"))));

            // Success message
            flash("message", "Note removed!");
        }

        return redirect(routes.NotesController.index());
    }
}
