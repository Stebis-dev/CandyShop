package com.CandyShop.hibernateControllers;

import com.CandyShop.model.*;
import com.CandyShop.model.Order;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomHib extends GenericHib {
    public CustomHib(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByCredentials(String login, String password) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(cb.and(cb.like(root.get("login"), login)));
            Query q = em.createQuery(query);

            User user = (User) q.getSingleResult();

            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
            return null;
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void createUser(User user) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Null value encountered");
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteProduct(int productId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            deleteProductFromCart(productId);
            deleteProductFromWarehouse(productId);

            for (Comment comment : getProductComments(productId)) {
                delete(Comment.class, comment.getId());
            }

            var product = em.find(Product.class, productId);
            em.remove(product);
            em.getTransaction().commit();
        } catch (NullPointerException e) {
            System.out.println("Null value encountered");
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteProductFromCart(int productId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaDelete<Cart> deleteQuery = cb.createCriteriaDelete(Cart.class);
            Root<Cart> root = deleteQuery.from(Cart.class);
            Predicate whereClause = cb.equal(root.get("product").get("id"), productId);
            deleteQuery.where(whereClause);
            em.createQuery(deleteQuery).executeUpdate();

            em.getTransaction().commit();
        } catch (NullPointerException e) {
            System.out.println("Null value encountered");
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteProductFromWarehouse(int productId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaDelete<WarehouseInventory> deleteQuery = cb.createCriteriaDelete(WarehouseInventory.class);
            Root<WarehouseInventory> root = deleteQuery.from(WarehouseInventory.class);
            Predicate whereClause = cb.equal(root.get("product").get("id"), productId);
            deleteQuery.where(whereClause);
            em.createQuery(deleteQuery).executeUpdate();

            em.getTransaction().commit();
        } catch (NullPointerException e) {
            System.out.println("Null value encountered");
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Cart> getUserCarts(int userId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Cart> query = cb.createQuery(Cart.class);
            Root<Cart> root = query.from(Cart.class);
            query.select(root).where(cb.equal(root.get("customer").get("id"), userId));

            TypedQuery<Cart> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public Cart getUserCartProduct(int userId, int productId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Cart> query = cb.createQuery(Cart.class);
            Root<Cart> root = query.from(Cart.class);
            query.select(root).where(cb.and(
                    cb.equal(root.get("customer").get("id"), userId),
                    cb.equal(root.get("product").get("id"), productId))
            );

            TypedQuery<Cart> typedQuery = em.createQuery(query);
            return typedQuery.getSingleResult();
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }
    //TODO create customHib for getCartsByProduct(int productID)

    public List<WarehouseInventory> getWarehouseInventory(int warehouseId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<WarehouseInventory> query = cb.createQuery(WarehouseInventory.class);
            Root<WarehouseInventory> root = query.from(WarehouseInventory.class);
            query.select(root).where(cb.equal(root.get("warehouse").get("id"), warehouseId));

            TypedQuery<WarehouseInventory> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteComment(int commentId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            var comment = em.find(Comment.class, commentId);

            em.remove(comment);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Null value encountered");
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteWarehouse(int warehouseId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var warehouse = em.find(Warehouse.class, warehouseId);
            deleteInventoryFromWarehouse(warehouseId);
            em.remove(warehouse);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteInventoryFromWarehouse(int warehouseId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaDelete<WarehouseInventory> deleteQuery = cb.createCriteriaDelete(WarehouseInventory.class);
            Root<WarehouseInventory> root = deleteQuery.from(WarehouseInventory.class);
            Predicate whereClause = cb.equal(root.get("warehouse").get("id"), warehouseId);
            deleteQuery.where(whereClause);
            em.createQuery(deleteQuery).executeUpdate();

            em.getTransaction().commit();
        } catch (NullPointerException e) {
            System.out.println("Null value encountered");
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Order> getUsersOrder(int userId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Order> query = cb.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            query.select(root).where(cb.equal(root.get("customer").get("id"), userId));
            TypedQuery<Order> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Order> getManagerOrder(int managerId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Order> query = cb.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            query.select(root).where(cb.equal(root.get("manager").get("id"), managerId));
            TypedQuery<Order> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public List<OrderDetails> getOrderDetails(int orderId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<OrderDetails> query = cb.createQuery(OrderDetails.class);
            Root<OrderDetails> root = query.from(OrderDetails.class);
            query.select(root).where(cb.equal(root.get("order").get("id"), orderId));

            TypedQuery<OrderDetails> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Comment> getProductComments(int productId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
            Root<Comment> root = query.from(Comment.class);
            query.select(root).where(cb.equal(root.get("product").get("id"), productId));

            TypedQuery<Comment> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Comment> getOrderComments(int oderId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
            Root<Comment> root = query.from(Comment.class);
            query.select(root).where(cb.equal(root.get("order").get("id"), oderId));

            TypedQuery<Comment> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteOrder(int orderId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            var order = em.find(Order.class, orderId);

            List<OrderDetails> orderDetails = getOrderDetails(orderId);
            for (OrderDetails orderDetail : orderDetails) {
                delete(OrderDetails.class, orderDetail.getId());
            }
            List<Comment> comments = getOrderComments(orderId);
            for (Comment comment : comments) {
                deleteComment(comment.getId());
            }

            em.remove(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Null value encountered");
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Order> getFilteredOrders(LocalDate startDate, LocalDate endDate, OrderStatus status, Manager manager) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Order> query = cb.createQuery(Order.class);
            Root<Order> order = query.from(Order.class);

            List<Predicate> predicates = new ArrayList<>();

            if (startDate != null && endDate != null) {
                predicates.add(cb.between(order.get("dateCreated"), startDate, endDate));
            }

            if (status != null) {
                predicates.add(cb.equal(order.get("status"), status));
            }

            if (manager != null) {
                predicates.add(cb.equal(order.get("manager"), manager));
            }

            query.where(cb.and(predicates.toArray(new Predicate[0])));
            TypedQuery<Order> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }
}
