package dgtic.unam.credencialesdelcliente

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dgtic.unam.credencialesdelcliente.data.Login
import dgtic.unam.credencialesdelcliente.data.UserClient
import dgtic.unam.credencialesdelcliente.data.Usuario
import dgtic.unam.credencialesdelcliente.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userClient: UserClient
    var builder = Retrofit.Builder()
        .baseUrl("http://192.168.100.201:8080/")
        .addConverterFactory(GsonConverterFactory.create())
    var retrofit = builder.build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userClient = retrofit.create(UserClient::class.java)
        supportActionBar?.hide()
        binding.login.setOnClickListener {
            login()
        }
    }
    private fun login() {
        var usuario=binding.username.text.toString()
        var password=binding.password.text.toString()
        if(!usuario.isEmpty()&&!usuario.isEmpty()){
            var login = Login(usuario, password)
            var call = userClient.login(login)
            call.enqueue(object:Callback<Usuario>{
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if(response.isSuccessful){
                        var token=response.body()?.token.toString()
                        Toast.makeText(binding.login.context,response.body()?.token.toString(),Toast.LENGTH_SHORT).show()
                        var paso: Intent = Intent(binding.login.context, SecondActivity::class.java).apply {
                            putExtra("token", token)
                        }
                        startActivity(paso)
                    }else{
                        Toast.makeText(binding.login.context,"Credenciales no validas",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(binding.login.context,"error :(",Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Toast.makeText(binding.login.context,"Introduzca las credenciales",Toast.LENGTH_SHORT).show()
        }
    }
}