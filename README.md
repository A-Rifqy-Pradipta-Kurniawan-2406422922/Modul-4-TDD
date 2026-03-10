# Eshop
Rifqy Pradipta Kurniawan - 2406422922

### URL Aplikasi
https://modul-2-rifqy-770a91ef4d62.herokuapp.com

## Module 1
### Reflection 1

#### 1. Clean Code

##### Meaningful Naming
The project follows the principle of meaningful names. Classes like ProductController, ProductService, and ProductRepository used to describe their roles. Methods are also phrases to explain their function, such as createProductPage, findById, and findAll, making the code easy to understand by others.

##### Functions
The functions in the code follow the core principles of modern software design.

*   Small & Do One Thing: Functions remain small and focused on a single responsibility.
    *   ProductController methods (createProductPost, editProductPost) only handle HTTP request/response logic. They receive data, appoint the logic work to the service, and determine which view to show.
    *   ProductRepository methods (create, delete) are responsible only for editing the productData list.
*   etc

##### Objects and Data Structures
The code shows a clear difference between objects and data structures.
*   The Product class acts as a data structure. It is a simple Object with primary purpose of holding data and expose it through getters and setters. It contains no other logic.
*   Class like ProductService and ProductRepository are the real objects. They provide behavior through methods (create(), findAll(), etc).

#### 2. Mistakes and Areas for Improvement

##### Comments
no comments have applied in this code
##### Error Handling: 
Error handling such as don't return null, use exceptions rather than return codes has not applied in this code.
##### Lack of Input Validation
The code currently has no input validation. A user can submit a form with an empty product name or a negative quantity.

### Reflection 2

#### Unit Testing

At first, writing tests feels like burden, but the value it provides is invaluable. It gave me a deeper understanding of what each class is supposed to do.

There is no specific number of unit tests for a class. The goal itself is to test a class thoroughly. A good metric maybe is to have at least one test for every method in the class. Furthermore, any conditional logic (like if/else) within a method provide multiple responses, and each path should be tested.

This is where coverage becomes used. Code coverage measure how much lines of your source code were executed by tests. If the coverage is low, it explains that most of the code are untested.

However, 100% code coverage does not mean the code don't have any bugs. Coverage only confirms that a line of code was executed, not that it is correct. 

#### Functional Tests

In my opinion, creating a new functional test class with the same setup and variables would be a direct violation of the DRY (Don't Repeat Yourself) principle. It results in code that's not clean and decrease the code quality because it is repetitive.

However, this can be done better by creating a base test class to do the same thing. The same functional tests can use again by extending this base class. This way, its become much easier to manage and update the test code because one file change only needed.

## Module 2
### Reflection 
> 1. List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.

Setelah menyelesaikan module 2 ini, saya telah menemukan dan memperbaiki beberapa permasalahan code quality dan security hotspot, seperti:
1. Menambahkan unit test pada layer controller, service, repository, dan application entry point hingga mencapai 100% code coverage pada instruction dan branch. Strategi yang saya gunakan adalah membaca report JaCoCo terlebih dahulu, lalu menambahkan test secara bertahap sesuai line dan branch yang belum tercover.
2. Menambahkan workflow SonarCloud Analysis pada setiap push agar proses scanning berjalan otomatis pada pipeline CI, sehingga issue bisa terdeteksi lebih awal sebelum merge.
3. Memperbaiki hotspot Sonar terkait secret expansion in run block dengan memindahkan konfigurasi sonar.organization dan sonar.projectKey ke build.gradle.kts pada bagian sonarqube, sehingga command workflow tidak lagi mengekspos nilai secret secara langsung karena organization key tidak dikategorikan sebagai secret.
4. Memperbaiki hotspot Sonar terkait dependency integrity dengan menambahkan gradle/verification-metadata.xml.
5. Memperbaiki hotspot Sonar pada GitHub Actions dengan melakukan pinning action ke full commit SHA sesuai sonarcloud.
6. Memperbaiki kegagalan deployment Heroku dengan menambahkan system.properties berisi java.runtime.version=21, sehingga runtime Heroku konsisten dan build tidak lagi gagal karena versi Java.

> 2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!

Berdasarkan workflow yang saya miliki saat ini, implementasi sudah memenuhi definisi Continuous Integration dan Continuous Deployment. Dari sisi CI, setiap perubahan kode diproses otomatis melalui GitHub Actions untuk menjalankan test, menghasilkan report coverage JaCoCo, dan analisis menggunakan SonarCloud. Hal ini membuat validasi kualitas kode menjadi konsisten dan mengurangi risiko error saat merge ke branch main.

Dari sisi CD, saya telah menerapkan deployment otomatis ke Heroku ketika terjadi push ke branch main menggunakan workflow deploy-heroku.yml. Dengan mekanisme ini, proses release tidak lagi manual karena pipeline langsung melakukan build dan deploy. Aplikasi juga telah berhasil terdeploy dan dapat diakses pada URL berikut:
https://modul-2-rifqy-770a91ef4d62.herokuapp.com

## Module 3
### Reflection 1
> Explain what principles you apply to your project

Setelah menyelesaikan module 3 ini, saya telah mengimplementasikan prinsip SOLID pada project sebagai berikut:
1. Single Responsibility Principle
Dengan memisahkan CarController dari ProductController sehingga setiap controller hanya fokus pada satu entitas. ProductController hanya menangani product, dan CarController hanya menangani car.
2. Open Closed Principle
Dengan membuat abstraction repository dan service agar perilaku sistem dapat diperluas tanpa harus mengubah struktur utama yang sudah ada. Implementasi baru dapat ditambahkan selama mengikuti interface yang sama.
3. Liskov Substitution Principle
Dengan menghapus inheritance CarController terhadap ProductController karena perilaku keduanya tidak sama. Dengan membuat keduanya independen, perilaku sistem menjadi lebih konsisten.
4. Interface Segregation Principle
Dengan memecah kontrak repository menjadi interface terpisah read dan write, sehingga class yang menggunakannya hanya bergantung pada method yang memang dibutuhkan.
5. Dependency Inversion Principle
Controller dan service diubah agar bergantung pada interface, bukan kode implementasi. Contohnya CarController bergantung ke CarService, dan service bergantung ke abstraction repository.

### Reflection 2
> Explain the advantages of applying SOLID principles to your project with examples

Menurut saya, terdapat beberapa keuntungan saat menerapkan SOLID pada project ini:
1. Struktur kode menjadi lebih rapi dan jelas
Dengan memisahkan ProductController dan CarController, tanggung jawab tiap class lebih jelas. Hal ini mempermudah saat membaca alur dan saat melakukan perubahan fitur.
2. Proses maintenance menjadi lebih mudah
Karena ketergantungan menjadi abstraction, perubahan pada detail implementasi tidak langsung berdampak ke banyak class. Ini membantu ketika ingin melakukan perubahan pada suatu class.
3. Risiko perubahan yang merusak fitur lain lebih kecil
Dengan prinsip SRP dan ISP, perubahan pada fitur car tidak perlu menyentuh logika product secara langsung, sehingga menjadi lebih isolated.

### Reflection 3
> Explain the disadvantages of not applying SOLID principles to your project with examples

Jika SOLID tidak diterapkan, ada beberapa dampak negatif yang cukup terasa:
1. Class menjadi terlalu besar dan sulit dipahami
Jika controller untuk car dan product tetap digabung, class akan terus bertumpuk dan membuat debugging lebih sulit.
2. Ketergantungan antar komponen menjadi terlalu kuat
Jika controller atau service langsung bergantung pada implementasi kode, perubahan kecil di satu tempat bisa memaksa perubahan di class lain.
3. Sulit mengembangkan fitur baru secara aman
Tanpa OCP dan ISP, penambahan fitur sering berujung mengubah banyak kode lama. Risiko regression menjadi lebih tinggi.
4. Sulit menjaga kualitas
Kode yang tidak terstruktur membuat testing, refactoring, dan kolaborasi tim menjadi sulit.

## Module 4
### Reflection

>1. Reflect based on Percival (2017) proposed self-reflective questions (in “Principles and Best Practice of Testing” submodule, chapter “Evaluating Your Testing Objectives”), whether this TDD flow is useful enough for you or not. If not, explain things that you need to do next time you make more tests.

Alur TDD sangat membantu dan cukup efektif untuk pengembangan fitur Order di modul ini. Dengan pola RED, GREEN, lalu REFACTOR, saya jadi lebih fokus pada alur yang benar terlebih dahulu, bukan langsung menulis implementasi yang tidak penting. Saat test gagal di fase RED, membuat kita tau fungsi yang belum terpenuhi, lalu di fase GREEN tinggal tulis kode minimum agar test lulus, kemudian REFACTOR.

>2. You have created unit tests in Tutorial. Now reflect whether your tests have successfully followed F.I.R.S.T. principle or not. If not, explain things that you need to do the next time you create more tests.

Secara umum, test sudah cukup mengikuti prinsip F.I.R.S.T. Pada Fast, seluruh unit test berjalan cepat karena tidak bergantung pada database atau external call. Dari sisi Independent, tiap test dibuat terpisah sehingga tidak bergantung urutan eksekusi test lain. Untuk Repeatable, hasil test konsisten saat dijalankan ulang. Dari sisi Self-Validating, assertion yang dipakai sudah menghasilkan status pass atau fail secara jelas tanpa validasi manual. Terakhir untuk Timely, test ditulis terlebih dahulu sebelum implementasi utama sesuai alur tutorial TDD.