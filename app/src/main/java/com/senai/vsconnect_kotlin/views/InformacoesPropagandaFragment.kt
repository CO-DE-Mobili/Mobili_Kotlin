package com.senai.vsconnect_kotlin.views

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.senai.vsconnect_kotlin.R
import com.senai.vsconnect_kotlin.adapters.ListaPropagandasAdapter
import com.senai.vsconnect_kotlin.apis.EndpointInterface
import com.senai.vsconnect_kotlin.apis.RetrofitConfig
import com.senai.vsconnect_kotlin.databinding.FragmentInformacoesPropagandaBinding
import com.senai.vsconnect_kotlin.models.Propaganda
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class InformacoesPropagandaFragment : Fragment(), ListaPropagandasAdapter.OnItemClickListener {

    private var _binding: FragmentInformacoesPropagandaBinding? = null
    private val binding get() = _binding!!

    private val clienteRetrofit = RetrofitConfig.obterInstanciaRetrofit()

    private val endpoints = clienteRetrofit.create(EndpointInterface::class.java)

    private var teste = ""




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInformacoesPropagandaBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun getLocationFromAddress(context: Context, strAddress: String): Pair<Double, Double>? {
            val geocoder = Geocoder(context)
            var latitude: Double = 0.0
            var longitude: Double = 0.0
            try {
                val addressList: List<Address>? = geocoder.getFromLocationName(strAddress, 1)
                if (!addressList.isNullOrEmpty()) {
                    val address: Address = addressList[0]
                    latitude = address.latitude
                    longitude = address.longitude
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return if (latitude != 0.0 && longitude != 0.0) {
                Pair(latitude, longitude)
            } else {
                null
            }
        }

// Suponha que você tenha um botão chamado "verRotasButton" no layout do seu fragmento
        binding.rotas.setOnClickListener {
//            val address = "R. Goitacazes, 279 - Centro, São Caetano do Sul - SP, 09510-300"
//            val location = getLocationFromAddress(requireContext(), address)
//            if (location != null) {
//                val latitude = location.first
//                val longitude = location.second
//                // Use latitude and longitude
//            } else {
//                // Failed to convert address to location
//            }

            // Latitude e longitude do destino
            val latitudeDestino = -23.58445
            val longitudeDestino = -46.72489

            // URI para abrir o Google Maps e traçar a rota para o destino
            val uri = "http://maps.google.com/maps?saddr=&daddr=$latitudeDestino,$longitudeDestino"

            // Criando o Intent
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//
//            startActivity(intent)

//        Toast.makeText(requireContext(), teste, Toast.LENGTH_SHORT).show()

            val sharedPreferences = requireContext().getSharedPreferences("idPropaganda", Context.MODE_PRIVATE)

            val idConteudo = sharedPreferences.getString("idPropaganda", "")

//        buscarPropagandaPorID(idConteudo.toString())
        }

        private fun buscarPropagandaPorID(idPropaganda: String) {
            val root: View = binding.root
            var propagandaObj: JSONObject;

            endpoints.buscarPropagandaPorID(UUID.fromString(idPropaganda)).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                TODO(
//                    "1 - Precisa atribuir os valores do objeto retornado API para suas respectivas Views" +
//                            "2 - Para o valor que retorna uma imagem utilizar o Picasso para exibi-la"
//                )


                    propagandaObj = JSONObject(response.body().toString())

                    val viewNomePropaganda = root.findViewById<TextView>(R.id.texto_nome_propaganda)
                    viewNomePropaganda.text = propagandaObj.getString("nome")

                    val viewDescricaoPropaganda = root.findViewById<TextView>(R.id.texto_descricao_propaganda)
                    viewDescricaoPropaganda.text = propagandaObj.getString("descricao")

                    val viewlocalizacaoPropaganda = root.findViewById<TextView>(R.id.texto_localizacao_propaganda)
                    // viewlocalizacaoPropaganda.text = propagandaObj.getString("localizacao")

                    val viewHorarioPropaganda = root.findViewById<TextView>(R.id.texto_funcionamento_propaganda)
                    viewHorarioPropaganda.text = propagandaObj.getString("duracao_parceria")

                    //val imagemPropaganda = root.findViewById<ImageView>(R.id.view_imagem_informacoes_propaganda)
                    // val urlImagem = "http://172.16.52.169:8080/img/" + propagandaObj.getString("url_img")

                    //Picasso.get().load(urlImagem).into(imagemPropaganda)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }


        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        override fun onItemClick(propaganda: Propaganda) {
//        teste = propaganda.toString()
//        Toast.makeText(requireContext(), "${propaganda.id}", Toast.LENGTH_SHORT).show()
        }
    }
}