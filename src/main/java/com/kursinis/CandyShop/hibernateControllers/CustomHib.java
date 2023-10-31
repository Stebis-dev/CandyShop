package com.kursinis.CandyShop.hibernateControllers;

import com.kursinis.CandyShop.model.Comment;
import com.kursinis.CandyShop.model.Product;
import com.kursinis.CandyShop.model.User;
import com.kursinis.CandyShop.model.Warehouse;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class CustomHib extends GenericHib {
    public CustomHib(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByCredentials(String login, String password) {
        EntityManager em = null;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), password)));
            Query q;

            q = em.createQuery(query);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteProduct(int productId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var product = em.find(Product.class, productId);
            var warehouse = product.getWarehouse();
            var cart = product.getCart();

            if (warehouse != null) {
                warehouse.getInStockProducts().remove(product);
                em.merge(warehouse);
            }
            if (cart != null) {
                cart.getItemsInCart().remove(product);
                em.merge(cart);
            }

            em.remove(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProductsByCartId(int cartId) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> root = query.from(Product.class);

            query.select(root).where(cb.equal(root.get("cart").get("id"), cartId));

            TypedQuery<Product> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();

        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Product> getAvailableProducts() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> root = query.from(Product.class);

            query.select(root).where(cb.isNull(root.get("cart").get("id")));

            TypedQuery<Product> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();

        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteComment(int commentId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var comment = em.find(Comment.class, commentId);
            var product = comment.getProduct();

            if (product != null) {
                product.getCommentList().remove(comment);
                em.merge(comment);
            }

            em.remove(comment);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteWarehouse(int warehouseId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var warehouse = em.find(Warehouse.class, warehouseId);
            List<Product> products = new ArrayList<>(warehouse.getInStockProducts());

            for (Product product : products) {
                warehouse.getInStockProducts().remove(product);
                deleteProduct(product.getId());
            }
            em.remove(warehouse);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }
}
