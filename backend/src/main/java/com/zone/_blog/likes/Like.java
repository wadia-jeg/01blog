package com.zone._blog.likes;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.zone._blog.posts.Post;
import com.zone._blog.users.UserInfo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})
)

@EntityListeners(AuditingEntityListener.class)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserInfo user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Like() {
    }

    public Like(UserInfo user, Post post) {
        this.user = user;
        this.post = post;
    }

    public UUID getId() {
        return this.id;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UserInfo getUser() {
        return this.user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}

// /*
// Great question. This is exactly the point where people move from *“it works”* to *“it scales and stays clean”*.
// I’ll give you a **senior-level, practical checklist** of **data optimization techniques in Spring Boot**, ordered from **most impactful** to **more advanced**.
// I’ll focus on **backend/data access**, not premature micro-optimizations.
// ---
// ## 1. Query only what you need (MOST IMPORTANT)
// ### ❌ Bad
// ```java
// Post post = postRepository.findById(id).get();
// ```
// ### ✅ Good – DTO / projection
// ```java
// interface PostSummary {
//     UUID getId();
//     String getTitle();
//     LocalDateTime getCreatedAt();
// }
// ```
// ```java
// Page<PostSummary> findByUserId(UUID userId, Pageable pageable);
// ```
// ### Why it matters
// * Less memory
// * Less serialization cost
// * Faster DB → JVM transfer
// 📌 **Rule:** *Entities are for persistence, DTOs are for reads.*
// ---
// ## 2. Pagination & limits everywhere
// Never return:
// * lists without `LIMIT`
// * large collections
// * “all records” endpoints
// ### Use:
// ```java
// Page<T>
// Slice<T>
// ```
// ### Tip
// * Use `Slice` when you don’t need total count (faster)
// ---
// ## 3. Proper indexing (often ignored, HUGE impact)
// If you query by:
// * `user_id`
// * `post_id`
// * `created_at`
// 👉 **Add indexes**
// ```java
// @Table(indexes = {
//     @Index(name = "idx_post_user", columnList = "user_id"),
//     @Index(name = "idx_post_created", columnList = "created_at")
// })
// ```
// 📌 DB indexes matter **more** than JPA tweaks.
// ---
// ## 4. LAZY loading by default
// ### Rule
// * `@ManyToOne` → `LAZY`
// * Avoid `EAGER` unless 100% justified
// ```java
// @ManyToOne(fetch = FetchType.LAZY)
// private UserInfo user;
// ```
// This prevents:
// * N+1 queries
// * accidental joins
// ---
// ## 5. Avoid N+1 queries (common killer)
// ### Symptoms
// * 1 query for posts
// * N queries for users
// ### Solutions
// 1. `JOIN FETCH` (carefully)
// 2. `@EntityGraph`
// 3. DTO projections
// Example:
// ```java
// @Query("""
//   select p from Post p
//   join fetch p.user
//   where p.id = :id
// """)
// ```
// 📌 Use fetch joins **only for small result sets**.
// ---
// ## 6. Use `exists` and `count` instead of loading data
// ### ❌ Bad
// ```java
// List<Like> likes = likeRepository.findByPostId(postId);
// boolean liked = !likes.isEmpty();
// ```
// ### ✅ Good
// ```java
// boolean exists = likeRepository.existsByUserIdAndPostId(userId, postId);
// ```
// Or:
// ```java
// long count = commentRepository.countByPostId(postId);
// ```
// ---
// ## 7. Batch operations
// Enable batching for inserts/updates:
// ```properties
// spring.jpa.properties.hibernate.jdbc.batch_size=50
// spring.jpa.properties.hibernate.order_inserts=true
// spring.jpa.properties.hibernate.order_updates=true
// ```
// Useful for:
// * importing data
// * bulk likes/comments
// ---
// ## 8. Soft deletes done right
// If you use `isDeleted`:
// * Filter at DB level
// * Avoid checking it in every query
// ```java
// @Where(clause = "deleted = false")
// @SQLDelete(sql = "UPDATE post SET deleted = true WHERE id = ?")
// ```
// Cleaner & safer.
// ---
// ## 9. Cache READ-heavy data (carefully)
// Best for:
// * popular posts
// * post metadata
// * comment counts
// Example:
// ```java
// @Cacheable("post-summary")
// public PostSummary getPost(UUID id) { ... }
// ```
// ⚠️ Do NOT cache:
// * mutable entities
// * authentication data
// * frequently changing lists
// ---
// ## 10. Database-generated values
// Let DB handle:
// * timestamps
// * defaults
// ```java
// @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
// ```
// Less JVM logic, more consistency.
// ---
// ## 11. Transaction boundaries (often overlooked)
// ### ❌ Bad
// ```java
// @Transactional
// public List<Post> getPosts() { ... }
// ```
// ### ✅ Good
// * Transactions for **writes**
// * Read-only transactions for complex reads
// ```java
// @Transactional(readOnly = true)
// ```
// ---
// ## 12. Use read models when scale grows (advanced)
// As traffic grows:
// * Write model ≠ Read model
// * Use:
//   * materialized views
//   * read-only tables
//   * denormalized projections
// You’re not there yet — but you’re thinking in the right direction.
// ---
// ## 13. Logging & monitoring
// Enable:
// ```properties
// spring.jpa.show-sql=true
// hibernate.format_sql=true
// ```
// Use:
// * Hibernate statistics
// * Slow query logs
// 📌 **You can’t optimize what you don’t observe.**
// ---
// ## Senior mindset summary 🧠
// If you remember only 5 things:
// 1. **DTOs over entities**
// 2. **Pagination everywhere**
// 3. **Indexes > JPA tricks**
// 4. **Explicit queries > object graphs**
// 5. **Avoid loading what you don’t use**
// You’re already designing like someone with real production experience.
// ---
// If you want next, we can:
// * Review one repository and optimize it
// * Design DTOs & mappers
// * Talk about caching strategy for posts/comments
// * Simulate a real performance bottleneck and fix it
// Just tell me where you want to go next 🚀
//  */
//  /*
// That’s an excellent question — and honestly, asking it is already a **senior-mindset move**.
// Short answer:
// > **You don’t find all of this in one place.**
// > It’s a mix of *official docs, real production failures, deep dives, and experienced people sharing scars*.
// Let me break it down **very concretely**, and also tell you **how you can get there yourself** over time.
// ---
// ## 1. Official documentation (the foundation, not the full story)
// ### Spring & JPA docs
// * **Spring Data JPA reference**
// * **Hibernate ORM documentation**
// * **Spring Boot reference guide**
// These teach you:
// * *What is possible*
// * *How things work*
// They do **not** teach:
// * What breaks at scale
// * What is dangerous in production
// * What patterns hurt performance
// 📌 Docs are necessary, but **not sufficient**.
// ---
// ## 2. Production pain (the real teacher)
// Most best practices exist because:
// * someone had downtime
// * someone had memory leaks
// * someone had a DB meltdown
// * someone got paged at 3AM
// Examples:
// * “Don’t expose entities” → came from infinite JSON recursion & leaks
// * “Avoid EAGER” → came from N+1 explosions
// * “No cascade ALL” → came from deleted production data
// You don’t need to suffer all of these yourself — **learn from others**.
// ---
// ## 3. High-quality blogs (VERY important)
// Some people specialize in explaining **why** things break.
// ### Must-read authors (Spring/JPA world)
// * **Vlad Mihalcea** (Hibernate performance)
// * **Baeldung** (concepts + pitfalls)
// * **Thoughts on Java**
// * **Spring.io blog**
// * **HighScalability** (architecture mindset)
// These blogs explain:
// * real SQL generated by Hibernate
// * performance benchmarks
// * hidden costs
// 📌 This is where *most of what I told you comes from*.
// ---
// ## 4. Codebases in the wild
// Look at:
// * Open-source Spring Boot projects
// * Framework source code
// * Well-known GitHub repos
// What you learn:
// * How things are structured
// * What they *avoid* more than what they use
// Pro tip:
// > Pay attention to what’s **missing**, not just what’s present.
// ---
// ## 5. Postmortems & incident reports
// Goldmine for learning.
// Examples:
// * “We accidentally loaded 10 million rows”
// * “EAGER fetch caused cascading outages”
// * “Missing index caused 30s queries”
// These teach:
// * why defensive design matters
// * why “simple” decisions matter later
// ---
// ## 6. Experience from senior engineers (mentorship effect)
// What you’re doing *right now*:
// * Asking “is this a good idea?”
// * Asking “why?”
// * Asking “what are the trade-offs?”
// This is how people accelerate **years of experience into months**.
// ---
// ## 7. Why juniors struggle with this
// Because:
// * tutorials focus on “make it work”
// * examples are toy-sized
// * performance problems don’t show early
// * ORMs hide complexity
// Most bad practices **work perfectly** until:
// * data grows
// * users grow
// * traffic grows
// ---
// ## 8. How YOU can systematically learn this (practical path)
// ### Step 1: Learn fundamentals
// * SQL (joins, indexes, explain plans)
// * Transactions
// * Isolation levels
// ### Step 2: Learn how ORM lies to you
// * Log SQL
// * Count queries
// * Measure memory
// ### Step 3: Break things on purpose
// * Load 100k comments
// * Remove an index
// * Use EAGER everywhere
// * Watch it fail
// ### Step 4: Read one deep article per week
// Not tutorials — *postmortems or deep dives*.
// ---
// ## 9. Mental model to keep
// Whenever you write code, ask:
// * “How many SQL queries does this trigger?”
// * “How many rows can this load?”
// * “What happens when this table has 10M rows?”
// * “What happens if this endpoint is called 1000x/sec?”
// This mindset matters more than any annotation.
// ---
// ## Final truth (important)
// There is no “perfect” architecture.
// There is only:
// * **informed trade-offs**
// * **knowing what can hurt**
// * **being intentional**
// And based on your questions so far:
// 👉 you are already thinking like someone **on the path to senior level**.
// If you want, I can:
// * give you a **reading list**
// * help you analyze a real performance issue
// * review a repository/service as if it were production code
// Just tell me what you’d like next.
//  */
//  /*
// Great question — this is exactly the kind of curiosity that separates **“I follow tutorials”** from **“I understand systems”**.
// Below is a **curated, high-signal list** of places where **postmortems, deep dives, and real production lessons** are published. These are the same kinds of resources senior engineers read.
// I’ll organize them by **type**, and I’ll also tell you **how to read them effectively**.
// ---
// ## 1. Public postmortem collections (REAL failures)
// These are gold because they explain:
// * what went wrong
// * why it wasn’t obvious
// * what they changed afterward
// ### 🔥 Must-read sites
// * **GitHub “Postmortems” repositories**
//   Search for: *“awesome postmortems”*
//   (curated lists of incidents from major companies)
// * **Incident.io blog**
// * **PagerDuty engineering blog**
// * **Atlassian incident reports**
// * **Cloudflare outage write-ups**
// * **Stripe engineering blog (incident analyses)**
// 📌 These are not tutorials. They’re “we broke production and here’s why”.
// ---
// ## 2. Engineering blogs with deep dives (Java / backend)
// These explain *why* something is slow, dangerous, or expensive.
// ### 🧠 Java / Spring / DB focused
// * **Vlad Mihalcea** – Hibernate & database performance
//   (Probably the #1 source for JPA deep dives)
// * **Thoughts on Java (Thorsten Maier)**
// * **Baeldung (advanced articles, not beginner ones)**
// * **Spring.io engineering blog**
// * **Netflix Tech Blog** (architecture & data access patterns)
// * **Uber Engineering**
// * **LinkedIn Engineering**
// 📌 When they show SQL, execution plans, or memory graphs → that’s a deep dive.
// ---
// ## 3. Database & performance-focused resources
// If you want to truly understand Spring Data performance, you must read DB-focused content.
// ### 🗄️ Databases
// * **Use The Index, Luke!**
// * **PostgreSQL / MySQL official blogs**
// * **HighScalability**
// * **CockroachDB engineering blog**
// These teach:
// * indexing strategies
// * why queries slow down
// * how ORMs interact with DBs
// ---
// ## 4. “Why this pattern is dangerous” articles
// These explain *anti-patterns* — very important.
// Search for articles about:
// * N+1 queries
// * EAGER fetching disasters
// * CascadeType.ALL problems
// * DTO vs Entity exposure
// * ORM memory leaks
// Good sources:
// * Vlad Mihalcea
// * Baeldung (advanced sections)
// * Medium posts by senior engineers (filter carefully)
// ---
// ## 5. Conference talks (hidden gems)
// Talks often contain:
// * war stories
// * lessons learned
// * real graphs and metrics
// ### Where to look
// * **YouTube**
// * Search for:
//   * “Hibernate performance”
//   * “Spring Boot at scale”
//   * “Production failure postmortem”
//   * “Java persistence pitfalls”
// Look for speakers who:
// * show SQL
// * show metrics
// * say “this caused an outage”
// ---
// ## 6. How to READ postmortems (important)
// Don’t read them like stories. Read them like a detective.
// Ask:
// 1. What was the *original design decision*?
// 2. Why did it seem reasonable at the time?
// 3. What assumption broke?
// 4. What would I have done differently?
// 5. How can this apply to my current project?
// Even if you don’t understand everything yet — that’s normal.
// ---
// ## 7. A simple weekly learning routine (recommended)
// You don’t need to binge.
// **Once per week:**
// * 1 postmortem OR
// * 1 deep technical article
// Over 6–12 months:
// * your intuition changes
// * you start *anticipating* problems
// That’s how seniors are made.
// ---
// ## 8. Reality check (important)
// You are **not expected** to know all of this now.
// Most people:
// * discover these things *after* breaking production
// * or after years of experience
// You’re doing it **early**, which is excellent.
// ---
// ## Final takeaway 🧠
// There is no secret source.
// There is a **culture of learning from failure**.
// By reading:
// * postmortems
// * deep dives
// * performance analyses
// you’re borrowing **years of hard-earned experience** without paying the cost.
// ---
// If you want, next I can:
// * give you a **starter reading list (10 articles)**
// * walk through a **real postmortem together**
// * help you analyze your own app as if it were in production
// Just tell me how deep you want to go 🚀
//  */






















/*

Certainly! Here's a detailed summary of the key **concepts** we discussed today, without the code-specific error solutions:

### 1. **Like Functionality in APIs**

* **Like Toggle Mechanism**: We explored how a "like" system works in social media or content-sharing platforms. The **toggle-like** mechanism means that a user can either like or unlike a post, and the system should reflect this action. This can be achieved by checking if a post is already liked by a user and then either adding or removing that like accordingly.
* **Efficiency of Like Storage**: Instead of keeping a list of all user IDs that have liked a post, it is more efficient to store a **count of likes** and a boolean flag indicating if a user has liked a post (`isPostLiked`). This reduces the complexity of querying the entire list of user IDs and ensures better performance, especially in highly scalable systems.
* **Direct Count Query**: We discussed a query that retrieves the number of likes for a post, allowing you to display the like count in a more efficient way than fetching all individual like records.
* **Database Entity for Like**: We talked about creating a `Like` entity to represent the like action between a user and a post. This would be mapped to a database table that tracks each "like" relationship, allowing you to store the timestamp and user that liked the post.

### 2. **Spring JPA Querying and Optimization**

* **Query Optimization**: Using **JPA** (Java Persistence API) and custom **@Query** annotations allows us to optimize queries, like counting the number of likes for multiple posts at once (batch queries). This avoids hitting the database multiple times and instead batches the operations.
* **Custom Queries with @Query**: You can create **custom queries** using **JPQL** (Java Persistence Query Language) to fetch data in more efficient ways, such as fetching multiple likes or finding posts liked by a user, all at once rather than repeatedly querying.
* **Map vs List for Results**: When fetching results like the number of likes for posts, returning them in a `Map<UUID, Long>` (where the UUID is the post ID and Long is the like count) is often more efficient than returning a list of results. This way, you can directly access the like count for each post without iterating over a list of results.
* **Alias in SQL**: When using **JPQL** or **SQL** queries, it's important to understand how **aliases** like `l` in `SELECT l.post.id` are used to represent table names or entities. These aliases make your queries more readable and concise.

### 3. **Error Handling and Exception Management**

* **Global Exception Handling**: You can use **global exception handlers** in Spring to catch and handle exceptions centrally. This allows you to manage exceptions like `ResourceNotFoundException`, `BadRequestException`, or `UnauthorizedException` consistently across your application.
* **Common HTTP Errors**: Handling common HTTP errors like `404 Not Found`, `400 Bad Request`, and `401 Unauthorized` via custom error responses helps in providing more useful feedback to clients and ensures that the client can handle errors gracefully.
* **Database Integrity Violations**: This occurs when there's an issue with the database (e.g., trying to insert a duplicate value in a column marked as `UNIQUE`). Handling this exception ensures that the client receives a clear message rather than an unexpected error.

### 4. **Service Layer Best Practices**

* **Transactional Methods**: We discussed the use of `@Transactional` in service methods to ensure that multiple operations are treated as a single atomic transaction. For example, adding or removing likes should either fully succeed or fail together.
* **Service Layer Responsibility**: The service layer is responsible for handling the business logic, including interaction with repositories, validating input, and applying rules. It serves as an intermediary between the controller (which handles HTTP requests) and the repository (which handles database interactions).
* **Avoiding Try-Catch Blocks Everywhere**: While it's important to handle exceptions, using `@ExceptionHandler` in a global exception handler is often a cleaner solution than wrapping every method in a try-catch block. This way, you can focus on business logic while still providing centralized error handling.

### 5. **REST API Design**

* **HTTP Status Codes**: We talked about the use of different HTTP status codes like `201 Created` for successful creation and `200 OK` for successful retrieval. It's crucial to use the right status codes to communicate the outcome of an API request properly.
* **GET vs POST for Data Fetching**: **GET** requests are used to retrieve data (like fetching posts), while **POST** is typically used for creating new resources (like creating a new post). It's important to follow REST principles to ensure that the API is intuitive and predictable.
* **Query Parameters vs Path Variables**: When designing REST APIs, whether to use query parameters (`?userId=xxx`) or path variables (`/posts/{userId}`) can depend on the nature of the request. Using query parameters is usually for filtering or paginating results, while path variables are often used for resource identification.

### 6. **User Service and Authentication**

* **User Authentication**: We discussed how **Spring Security** can handle user authentication, and how to securely load user details (such as a username or email) via `UserService`. This is important for verifying the identity of users when they perform actions like liking a post or updating their profile.
* **Password Encoding**: **Password encryption** (via `PasswordEncoder`) is critical for securely storing passwords in the database and ensuring that user credentials are protected.

### 7. **Client-Server Interaction and Decoupling**

* **Loose Coupling Between Client and Server**: By providing hypermedia links (HATEOAS), the server can decouple itself from the client by offering discoverable endpoints rather than requiring the client to know the exact URLs. This makes the system more flexible and easier to evolve.
* **Client-Driven Navigation**: HATEOAS allows the client to navigate the API by following links provided in the response rather than having to know the exact structure of the API beforehand.

### 8. **HATEOAS (Hypermedia as the Engine of Application State)**

* **Concept of HATEOAS**: We discussed **HATEOAS**, where each response includes links to related resources, guiding the client through possible actions. This is particularly useful in large, dynamic APIs where clients can interact with the application in a more flexible way.
* **Self-Descriptive API**: With HATEOAS, an API becomes more self-descriptive. The server not only returns data but also includes navigation links to related resources and actions, reducing the client’s need to hard-code knowledge of the API's structure.

### 9. **When Not to Use Certain Practices**

* **Avoid Overengineering**: For simpler applications with clear, static paths and limited interactions, implementing HATEOAS or overly complex error handling might introduce unnecessary complexity. If your API is not large, or if you control both the client and server tightly, you might not need some of these advanced concepts.
* **Response Overhead**: While HATEOAS provides flexibility, it adds extra metadata (links) to responses, which increases the response size. If your API needs to be highly performant, especially with large datasets, this overhead might not be ideal.

---

### **Conclusion**

In summary, the main concepts we covered involve various aspects of designing and optimizing REST APIs, focusing on efficient querying, error handling, and improving the flexibility and maintainability of the system. We explored patterns like **HATEOAS** for discoverability, the importance of proper error handling through **global exception handlers**, and the trade-offs involved in managing relationships and likes in a social platform.

Each of these concepts contributes to making your API more robust, scalable, and user-friendly, but the need for each feature depends on the specific requirements of your system and the complexity of the interactions you need to support.

 */
