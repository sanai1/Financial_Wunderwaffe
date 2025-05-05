package com.example.financialwunderwaffe.main.briefcase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.financialwunderwaffe.retrofit.database.asset.Asset
import com.example.financialwunderwaffe.retrofit.database.asset.AssetApiClient
import com.example.financialwunderwaffe.retrofit.database.asset.information.AssetInformationApiClient
import com.example.financialwunderwaffe.retrofit.database.asset.information.model.AssetInformation
import com.example.financialwunderwaffe.retrofit.database.asset.information.model.AssetPrice
import com.example.financialwunderwaffe.retrofit.database.asset.information.model.AssetTransaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class BriefcaseViewModel : ViewModel() {
    private lateinit var update: () -> Unit
    fun setUpdate(newUpdate: () -> Unit) {
        update = newUpdate
    }

    private val _listAssets = MutableLiveData<List<Asset>>()
    val listAssets: LiveData<List<Asset>> = _listAssets

    private val _selectAsset = MutableLiveData<Asset>()
    val selectAsset: LiveData<Asset> = _selectAsset

    private val _listAssetInformation = MutableLiveData<List<AssetInformation>>()
    val listAssetInformation: LiveData<List<AssetInformation>> = _listAssetInformation

    fun updateListInformation(token: String) = CoroutineScope(Dispatchers.IO).launch {
        val response = AssetInformationApiClient.assetInformationAPIService.getInformationByAssetId(
            token,
            _selectAsset.value!!.id
        ).execute()
        if (response.isSuccessful && response.body() != null) {
            _listAssetInformation.postValue(response.body())
        }
    }

    fun clearListInformation() {
        _listAssetInformation.value = emptyList()
    }

    fun createAssetTransaction(token: String, uid: UUID, assetTransaction: AssetTransaction) =
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                AssetInformationApiClient.assetInformationAPIService.createAssetTransaction(
                    token,
                    assetTransaction
                ).execute()
            if (response.isSuccessful && (response.body() ?: 0) > 0) {
                if (_listAssetInformation.value != null) {
                    updateListInformation(token)
                    update()
                }
            }
        }

    fun createAssetPrice(token: String, uid: UUID, assetPrice: AssetPrice) =
        CoroutineScope(Dispatchers.IO).launch {
            val response = AssetInformationApiClient.assetInformationAPIService.createAssetPrice(
                token,
                assetPrice
            ).execute()
            if (response.isSuccessful && (response.body() ?: 0) > 0) {
                if (_listAssetInformation.value != null) {
                    updateListInformation(token)
                    update()
                }
            }
        }

    fun selectAsset(newAsset: AssetState) {
        _selectAsset.value = _listAssets.value!!.first { it.id == newAsset.id }
    }

    fun updateListAssets(token: String, uid: UUID) = CoroutineScope(Dispatchers.IO).launch {
        val response = AssetApiClient.assetAPIService.getByUserUID(token, uid).execute()
        if (response.isSuccessful && response.body() != null) {
            _listAssets.postValue(response.body())
            if (_selectAsset.value != null) {
                if (_selectAsset.value!!.id in response.body()!!.map { it.id }) {
                    _selectAsset.postValue(
                        response.body()!!.first { it.id == _selectAsset.value!!.id })
                }
            }
        }
    }

    fun createAsset(asset: Asset, token: String, uid: UUID) =
        CoroutineScope(Dispatchers.IO).launch {
            val response = AssetApiClient.assetAPIService.createAsset(token, asset).execute()
            if (response.isSuccessful && (response.body() ?: 0) > 0) {
                if (_listAssets.value != null) {
                    update()
                }
            }
        }
}