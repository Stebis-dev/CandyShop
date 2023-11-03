package com.CandyShop.hibernateControllers;

import com.CandyShop.model.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

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
//            var warehouse = product.getWarehouse();

//            if (warehouse != null) {
//                warehouse.getInStockProducts().remove(product);
//                em.merge(warehouse);
//            }

            em.remove(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProductsFromCart(int userId) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Cart> root = query.from(Cart.class);
            Join<Cart, Product> cartProductJoin = root.join("product");
            query.select(cartProductJoin).where(cb.equal(root.get("customer").get("id"), userId));

            TypedQuery<Product> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Product> getProductsFromWarehouse(int warehouseId) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<WarehouseInventory> root = query.from(WarehouseInventory.class);
            Join<WarehouseInventory, Product> cartProductJoin = root.join("product");
            query.select(cartProductJoin).where(cb.equal(root.get("warehouse").get("id"), warehouseId));

            TypedQuery<Product> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Product> getNotAssignedProducts(int warehouseId) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<WarehouseInventory> root = query.from(WarehouseInventory.class);
            Join<WarehouseInventory, Product> cartProductJoin = root.join("product");
            query.select(cartProductJoin).where(cb.isNull(root.get("warehouse").get("id")));

            TypedQuery<Product> typedQuery = em.createQuery(query);

            return typedQuery.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteProductFromCart(int userId, int productId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaDelete<Cart> deleteQuery = cb.createCriteriaDelete(Cart.class);
            Root<Cart> root = deleteQuery.from(Cart.class);

            Predicate whereClause = cb.and(
                    cb.equal(root.get("customer").get("id"), userId),
                    cb.equal(root.get("product").get("id"), productId)
            );

            deleteQuery.where(whereClause);

            em.createQuery(deleteQuery).executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProductFromWarehouse(int warehouseId, int productId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaDelete<WarehouseInventory> deleteQuery = cb.createCriteriaDelete(WarehouseInventory.class);
            Root<WarehouseInventory> root = deleteQuery.from(WarehouseInventory.class);

            Predicate whereClause = cb.and(
                    cb.equal(root.get("warehouse").get("id"), warehouseId),
                    cb.equal(root.get("product").get("id"), productId)
            );

            deleteQuery.where(whereClause);

            em.createQuery(deleteQuery).executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
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
//            em.getTransaction().begin();
//            var warehouse = em.find(Warehouse.class, warehouseId);
//            List<Product> products = new ArrayList<>(warehouse.getInStockProducts());
//
//            for (Product product : products) {
//                warehouse.getInStockProducts().remove(product);
//                deleteProduct(product.getId());
//            }
//            em.remove(warehouse);
//            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }


}
