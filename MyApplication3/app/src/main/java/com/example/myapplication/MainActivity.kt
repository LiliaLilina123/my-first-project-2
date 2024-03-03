package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


// Объявление класса MainActivity, который расширяет AppCompatActivity
class MainActivity : AppCompatActivity() {

    // Поле для работы с Firebase Authentication
    private lateinit var auth: FirebaseAuth

    // Переопределение метода onCreate при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        // Вызов родительского метода onCreate
        super.onCreate(savedInstanceState)

        // Установка макета (layout) для активности из файла R.layout.activity_login
        setContentView(R.layout.activity_login)

        // Инициализация Firebase Authentication
        auth = Firebase.auth

        // Инициализация элементов пользовательского интерфейса по их идентификаторам (R.id)
        val loginEditText: EditText = findViewById(R.id.loginfield)
        val passwordEditText: EditText = findViewById(R.id.passwordfield)
        val showPasswordCheckBox: CheckBox = findViewById(R.id.showPasswordCheckBox)
        val buttonSubmit: Button = findViewById(R.id.buttonReg)
        val buttonReg: Button = findViewById(R.id.buttonLogIn2)

        // Установка слушателя на чекбокс
        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            // В зависимости от состояния чекбокса, устанавливаем или снимаем видимость пароля
            val inputType = if (isChecked) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            passwordEditText.inputType = inputType
            // Необходимо установить курсор в конец текста после изменения типа ввода
            passwordEditText.text?.let { passwordEditText.setSelection(it.length) }
        }

        // Слушатель для кнопки "Регистрация"
        buttonReg.setOnClickListener {
            // Переход к активности RegAct
            startActivity(Intent(this, RegAct::class.java))
        }

        // Слушатель для кнопки "Войти"
        buttonSubmit.setOnClickListener {
            // Получение введенных данных из полей ввода
            val log = loginEditText.text.toString()
            val pas = passwordEditText.text.toString()

            // Проверка наличия данных в полях ввода
            if(log.isEmpty() || pas.isEmpty()){
                // Вывод сообщения об ошибке при отсутствии данных
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Попытка входа через Firebase Authentication
            auth.signInWithEmailAndPassword(log, pas)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // успешный вход, переход в MainActivity
                        Toast.makeText(baseContext, "Authentication Success.",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LogIn, List::class.java))
                    } else {
                        // Если вход не удался, выводим сообщение об ошибке
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}