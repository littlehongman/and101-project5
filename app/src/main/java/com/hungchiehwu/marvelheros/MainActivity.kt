package com.hungchiehwu.marvelheros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var characterImageURL = ""
    private val heroIdList = arrayOf("1016181", "1009368", "1009220", "1009718", "1009664", "1009351")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val switchBtn = findViewById<Button>(R.id.switchBtn)
        val characterImg = findViewById<ImageView>(R.id.characterImg)

        val characterName = findViewById<TextView>(R.id.characterName)
        val characterDesc = findViewById<TextView>(R.id.characterDesc)


        switchBtn.setOnClickListener {
            // Get character name, description, imgURL
            getMarvelCharacter(characterImg, characterName, characterDesc)
        }
    }


    private fun getMarvelCharacter(characterImg: ImageView, characterNameView: TextView, characterDescView: TextView) {
        val client = AsyncHttpClient()
        val randomHeroId = heroIdList.random()

        client["https://gateway.marvel.com/v1/public/characters/$randomHeroId?ts=1&apikey=b45772c67810180b2d9f96998556faeb&hash=f28293a1beb528ee6132d56574bbe62c", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val characterObj = json.jsonObject.getJSONObject("data").getJSONArray("results").getJSONObject(0)

                Log.d("marvelObj", characterObj.toString());

                characterImageURL = characterObj.getJSONObject("thumbnail").getString("path") + ".jpg"
                characterImageURL = characterImageURL.replaceFirst("http", "https")

                Log.d("marvelImgURL", characterImageURL)

                val characterName = characterObj.getString("name")
                val characterDesc = characterObj.getString("description")

                characterNameView.text = characterName
                characterDescView.text = characterDesc

                Glide.with(this@MainActivity)
                    .load(characterImageURL)
                    .into(characterImg)

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Character Load Error", errorResponse)
            }
        }]
    }
}