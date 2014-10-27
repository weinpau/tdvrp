package de.hszg.tdvrp.core.solver;

import de.hszg.tdvrp.core.model.Customer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a partial solution (route). The route includes a simple
 * list of customers, which must be approached in the order of the list.
 *
 * @author weinpau
 */
public final class Route implements Serializable {

    private final List<Customer> customers = new ArrayList<>();

    private Route() {
    }

    public Route(List<Customer> customers) {
        this.customers.addAll(customers);
    }

    /**
     * Returns the list of customers.
     *
     * @return list of customers
     */
    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

}
