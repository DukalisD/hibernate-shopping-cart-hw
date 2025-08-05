package mate.academy.service.impl;

import java.util.ArrayList;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new RuntimeException("Can't find cart by user " + user));
        shoppingCart.getTickets().add(ticket);
        shoppingCartDao.update(shoppingCart);
    }
    // проблема в цьому методі, Optional is empty

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new RuntimeException("Can't find shopping cart by user: "
                        + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(user.getId());
        shoppingCart.setTickets(new ArrayList<>());
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (shoppingCart != null && shoppingCart.getTickets() != null) {
            shoppingCart.getTickets().clear();
            shoppingCartDao.update(shoppingCart);
        }
    }
}
