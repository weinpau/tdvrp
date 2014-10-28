## Solver for time-dependent VRPs ##

This project is a student project which is developed as part of a research project at the [Hochschule Zittau/Görlitz](http://www.hszg.de/).

The goal is to realize an optimization algorithm for time-dependent VRPs. In contrast to classical VRPs the tour of vehicles between two nodes is influenced through additional constraints. The instances of A. M. Figliozzi [[2011](http://www.sciencedirect.com/science/article/pii/S1366554511001426)], be used for the evaluation.

The solution and optimization process is divided into two phases:

1. Routing (solver) - the goal is to find a valid best possible order of tasks / customers.
2. Scheduling - the goal is to determine the optimal travel times between the customer and the start times of the tasks.


### Features ###


#### Implemented Solvers ####

- [Dummy Solver](tdvrp-solver.dummy/) - a simple solver, which distributes tasks without optimization on the available vehicles

#### Implemented Schedulers ####


- [Straight Scheduler](tdvrp-scheduler.straight/) - the resulting schedule is directly and is determined without optimizing the traveling time

#### Server ####

The [server subproject](/tdvrp-server) provides a simple REST interface, which was realized with the [Jersey](https://jersey.java.net/) framework and the [Jackson](http://jackson.codehaus.org/) JSON processor. The HTTP server is based on an embedded [Grizzly](https://grizzly.java.net/) HTTP server.

### How to use it ###

To use this project you need an JDK 1.8 and Apache Maven. Then you can build the project with the following command in the root directory:

	mvn clean install

#### Workflow example ####

The following sample code is also available in the sub-project [tdvrp-example](tdvrp-example/).

	// load the instance
    Instance instance = Instances.getInstanceByName("025_C101").get();

    // create the time-dependent function
    TDFunctionFactory tdFunctionFactory = TDFunctionFactories.getFactoryByName("TD1a").get();
    TDFunction tdFunction = tdFunctionFactory.createTDFunction(instance);

    // instantiate the solver
    Solver solver = new DummySolver();

    // search for a solution
    Solution solution = solver.solve(instance, tdFunction).orElse(null);

    if (solution == null) {
    	//No solution found
    }

    // instantiate the scheduler
    Scheduler scheduler = new StraightScheduler();

    // create a schedule for the solution 
    Schedule schedule = scheduler.schedule(solution).orElse(null);

    if (schedule == null) {
    	//No schedule found
    }

    System.out.printf("selected solver: %s%n", solver.getName());
    System.out.printf("selected scheduler: %s%n", scheduler.getName());
    System.out.printf("number of vehicles: %d%n", schedule.getVehicleSchedules().size());
    System.out.printf("traveling time: %f%n", schedule.getTravelingTime());
    System.out.printf("total distance: %f%n", schedule.getTotalDistance());
