## Solver for time-dependent VRPs ##

This project is a student project which is developed as part of a research project at the [Hochschule Zittau/Görlitz](http://www.hszg.de/).

The vehicle routing problem with time windows (VRPTW) models many practical optimisation problems in logistics or maintenance of geographically distributed organisations. In practice, the classical model is often not adequate because constant travel times between locations are assumed. Time-varying factors, such as traffic conditions or weather have a significant impact on the actual travel time. Therefore, the travel time between two locations, depends on the specific departure time. To represent these external influences, the VRPTW is extended to a time-dependent vehicle routing problem with time windows (TDVRP). In this case the changing driving time is represented by a time-dependent function. 

The present research results are limited to a consideration of TDVRP with hard time windows. This means, the service must necessarily start in the given time window. It follows that an early arrival or later departure is feasible. For evaluation of the results, we consider recently published time-dependent problem instances of A. M. Figliozzi [[2011](http://www.sciencedirect.com/science/article/pii/S1366554511001426)], where the classical Solomon instances are combined with time-dependent speed models.

The solution and optimization process is divided into two phases:

1. Routing (solver) - the goal is to find a valid best possible order of tasks / customers.
2. Scheduling - the goal is to determine the optimal travel times between the customer and the start times of the tasks.


### Features ###


#### Implemented Solvers ####

- [Dummy Solver](tdvrp-solver.dummy/) - a simple solver, which distributes tasks without optimization on the available vehicles

#### Implemented Schedulers ####


- [Straight Scheduler](tdvrp-scheduler.straight/) - the resulting schedule is directly and is determined without optimizing the travel time

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
    System.out.printf("travel time: %f%n", schedule.getTravelTime());
    System.out.printf("total distance: %f%n", schedule.getTotalDistance());
