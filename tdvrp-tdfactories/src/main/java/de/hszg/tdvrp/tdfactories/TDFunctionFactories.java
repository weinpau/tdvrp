package de.hszg.tdvrp.tdfactories;

import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;
import de.hszg.tdvrp.tdfactories.periodical.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
public final class TDFunctionFactories {

    private static final List<TDFunctionFactory> factories = new ArrayList<>();

    private TDFunctionFactories() {
    }

    static {
        factories.add(new DefaultTDFunctionFactory());

        factories.add(new TD1a());
        factories.add(new TD2a());
        factories.add(new TD3a());

        factories.add(new TD1b());
        factories.add(new TD2b());
        factories.add(new TD3b());

        factories.add(new TD1c());
        factories.add(new TD2c());
        factories.add(new TD3c());

        factories.add(new TD1d());
        factories.add(new TD2d());
        factories.add(new TD3d());
    }

    public static List<TDFunctionFactory> getFactories() {
        return Collections.unmodifiableList(factories);
    }

    public static Optional<TDFunctionFactory> getFactoryByName(String factoryName) {
        if (factoryName == null) {
            return Optional.empty();
        } else {
            return getFactories().stream().
                    filter(f -> factoryName.equals(f.getName())).
                    findAny();
        }

    }
}
