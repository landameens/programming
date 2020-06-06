package controller;

import controller.command.Command;
import controller.command.factory.CommandFactory;
import controller.command.factory.CommandMediator;
import controller.components.serviceMediator.Service;
import controller.components.serviceMediator.ServiceMediator;
import controller.components.serviceMediator.sharedData.SharedDataService;
import org.apache.commons.configuration2.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * This class allow you to build controller of your own.
 */
public final class ControllerBuilder {
    private final AbstractController abstractController;


    public ControllerBuilder(Configuration configuration,
                             Map<String, Class<? extends Command>> commands) {
        CommandMediator commandMediator = new CommandMediator(commands);
        this.abstractController = new AbstractController(configuration,
                                                         new CommandFactory(),
                                                         commandMediator);
    }


    /**
     * Use it if you need to add services support for commands.
     */
    public ControllerBuilder buildServiceMediator(Set<Service> services) {
        services.add(new SharedDataService());

        ServiceMediator serviceMediator = abstractController.getServiceMediator();

        if (serviceMediator != null) {
            services.forEach(serviceMediator::add);
        } else {
            serviceMediator = new ServiceMediator(services);
        }

        abstractController.setServiceMediator(serviceMediator);
        return this;
    }

    /**
     * Use it like a terminal operation.
     */
    public AbstractController build() {
        return abstractController;
    }
}
