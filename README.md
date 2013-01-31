Introduction
============

Play 2 plugin to use Morphia ORM in asynchronous way.


Features
========

* Automatically initializes Morphia at startup/reloading
* Automatically maps entities
* Automatically create collections and indexes.
* Configure Morphia via Play configuration file
* Asynchronous queries/updates/inserts using akka

Installation
============

Add dependency to your `project/Build.scala`:
  
	val appDependencies = Seq(
		javaCore,
		"morplay"   % "morplay_2.10" % "0.5.1"
	)
	
	object Resolvers {
		val githubRepository = Resolver.url("GitHub morplay repository", url("http://ladoch.github.com/repository/"))(Resolver.ivyStylePatterns)
	}
	
	val main = play.Project(appName, appVersion, appDependencies).settings(
		resolvers ++= Seq(DefaultMavenRepository, Resolvers.githubRepository)
	)

Add plugin to `conf/play.plugins`:

	5000:net.onlite.morplay.MorplayPlugin

Run `play update` for your project. It's all!


Using
=====

Get collection
--------------

	// Get collection
	MongoCollection<Note> collection = MorplayPlugin.store().collection(Note.class);
	
	// Get collection from "users" database
	MongoCollection<User> collection = MorplayPlugin.store("users").collection(User.class);
	

Query
--------

	// Get latest notes
	F.Promise<List<Note>> promise = collection.find()
										.order("-date")
										.limit(20)
										.asList();
											
	// Get note by id
	F.Promise<Note> promise = collecion.findById(id);

Asynchronous iterators
----------------------

Allow asynchronous processing query results before query finishes:

	// Get iterable object
	AsyncItarable<Note> iterable = collection.find().fetch();
	
	// Create task
	Runnable task = iterable.forEach(new Callable<Note> {
		// Process note
		...
	});
	
	// Schedule task to run every day
	Akka.system().scheduler().schedule(
  		Duration.create(0, TimeUnit.MILLISECONDS),
  		Duration.create(24, TimeUnit.HOURS)
  		task, 
  		"notesTask"
  	);


Updates
-------
	
	// Mark all notes as read
	F.Promise<UpdateResults<Note>> promise = collection.atomicAll().set("read", true).update(false);
	
	// Update note text
	F.Promise<UpdateResults<Note>> promise = collection.atomic(new Filter("_id", id))
													.set("text", newText)
													.update(false);

Inserts
-------

	// Inset new note
	F.Promise<Key<Note>> promise = collection.create(note);
	

	
		
