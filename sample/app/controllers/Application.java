package controllers;

import net.onlite.morplay.MorplayPlugin;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        MorplayPlugin.connection();
        return ok(index.render("Your new application is ready."));
    }
  
}