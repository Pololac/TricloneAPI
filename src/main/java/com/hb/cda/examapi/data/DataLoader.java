package com.hb.cda.examapi.data;

import com.hb.cda.examapi.entity.Expense;
import com.hb.cda.examapi.entity.User;
import com.hb.cda.examapi.entity.Account;

import com.hb.cda.examapi.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private EntityManager em;

    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Étape 1 : rechercher ou créer les utilisateurs
        String rawPassword = "123456";
        String hashed = passwordEncoder.encode(rawPassword);

        User user1 = findOrCreateUser("paul", hashed);
        User user2 = findOrCreateUser("pierre", hashed);
        User user3 = findOrCreateUser("jules", hashed);

        // Étape 2 : rechercher ou créer le groupe
        Account account1 = findOrCreateAccount("Ski Fev 2025", user1, user2, user3);
        Account account2 = findOrCreateAccount("WE Forez Juin 2025");

        // Étape 3 : créer des dépenses **uniquement si absentes**
        createExpenseIfNotExists("Forfait remontées mécaniques – Journée", 50.00,
                LocalDateTime.of(2025, 2, 14, 9, 30), user1, account1);

        createExpenseIfNotExists("Location des skis (matériel complet)", 30.00,
                LocalDateTime.of(2025, 2, 14, 9, 45), user2, account1);

        createExpenseIfNotExists("Déjeuner au chalet", 70.00,
                LocalDateTime.of(2025, 2, 14, 12, 15), user2, account1);

        em.flush();

/*        System.out.println(
                "CHECK repository → " +
                        userRepo.findByUsernameIgnoreCase("paul")
        );
        User paul = userRepo.findByUsernameIgnoreCase("paul").get();
        boolean ok = passwordEncoder.matches("123456", paul.getPassword());
        System.out.println(">>> password matches ? " + ok);*/
    }

    private User findOrCreateUser(String username, String passwordHash) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream().findFirst().orElse(null);
        if (user == null) {
            user = new User(username, passwordHash);
            em.persist(user);
        }
        return user;
    }

    private Account findOrCreateAccount(String name, User... members) {
        Account account = em.createQuery("SELECT a FROM Account a WHERE a.name = :name", Account.class)
                .setParameter("name", name)
                .getResultStream().findFirst().orElse(null);
        if (account == null) {
            account = new Account(name);
            for (User user : members) {
                account.addUser(user);
            }
            em.persist(account);
        }
        return account;
    }

    private void createExpenseIfNotExists(String description, double amount, LocalDateTime date, User payer, Account account) {
        boolean exists = em.createQuery(
                        "SELECT e FROM Expense e WHERE e.description = :desc AND e.amount = :amount AND e.date = :date AND e.payer = :payer AND e.account = :account", Expense.class)
                .setParameter("desc", description)
                .setParameter("amount", amount)
                .setParameter("date", date)
                .setParameter("payer", payer)
                .setParameter("account", account)
                .getResultStream()
                .findFirst().isPresent();

        if (!exists) {
            Expense e = new Expense();
            e.setDescription(description);
            e.setAmount(amount);
            e.setDate(date);
            e.setPayer(payer);
            e.setAccount(account);
            em.persist(e);
        }
    }
}
