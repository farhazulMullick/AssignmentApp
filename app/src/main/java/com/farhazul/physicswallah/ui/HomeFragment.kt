package com.farhazul.physicswallah.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.farhazul.physicswallah.adapter.HomeAdapter
import com.farhazul.physicswallah.constant.Constant
import com.farhazul.physicswallah.databinding.FragmentHomeBinding
import com.farhazul.physicswallah.model.Teacher
import com.farhazul.physicswallah.view.checkConnection
import java.sql.Array


class HomeFragment : Fragment() {
    private  var _binding: FragmentHomeBinding? = null;
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter
    var alertDialog : AlertDialog? = null

    private var teacherList = MutableLiveData<ArrayList<Teacher>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        setRecyclerView()
        checkInternet()
        return binding.root
    }

    private fun setRecyclerView() {
        homeAdapter = HomeAdapter()
        binding.rvShimmer.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(activity as Context)
        }
    }

    override fun onStart() {
        // checking for internet connection onStart()

        super.onStart()
    }

    private fun showLoading(){
        binding.progressLayout.visibility = View.VISIBLE
    }

    private fun stopLoading(){
        binding.progressLayout.visibility = View.GONE
    }

    private fun checkInternet() {
        showLoading()
        Log.d(TAG, "checkConnection -> ${requireContext().checkConnection()}")
        if (requireContext().checkConnection()){
            alertDialog?.dismiss()

            // if proper connection is found parse the data.
            pareseData()
        }
        else{
            // else show Retry alert dialog
            stopLoading()
            alertDialog = AlertDialog.Builder(requireContext())
                .setTitle("No Internet Connection")
                .setPositiveButton("Retry"){text, listener ->
                    checkInternet()
                }
                .setNegativeButton("Exit"){text, listener ->
                    ActivityCompat.finishAffinity(requireActivity())
                }
                .setCancelable(false)
                .create()
            alertDialog!!.show()
        }
    }

    private fun pareseData() {
        // Mutable live data to observe change of data in the list
        teacherList = MutableLiveData<ArrayList<Teacher>>()

        // temporary list
        val tempList = ArrayList<Teacher>()

        // using volley Library for parsing
        val queue = Volley.newRequestQueue(requireContext())
        val jsonArrayRequest = object: JsonArrayRequest(Constant.BASE_URL, {

            try{
                // on success stop loading
                stopLoading()
                // it is a jsonArray
                for(i in 0 until it.length()){
                    val jsonObject = it.getJSONObject(i)

                    val id = jsonObject.getInt("id")
                    val name = jsonObject.getString("name")
                    val subjects = jsonObject.getJSONArray("subjects")
                    val subList = ArrayList<String>()

                    // parsing subject is jsonArry
                    for (j in 0 until subjects.length()){
                        val subName = subjects.getString(j)
                        subList.add(subName)
                    }

                    val qualifications = jsonObject.getJSONArray("qualification")
                    val qualiList = ArrayList<String>()

                    // parsing qualifications json array
                    for(j in 0 until qualifications.length()){
                        val qualifiName = qualifications.getString(j)
                        qualiList.add(qualifiName)
                    }

                    val profileImg = jsonObject.getString("profileImage")
                    val teacher = Teacher(
                        id = id,
                        name= name,
                        subjects = subList,
                        qualifications = qualiList,
                        profileImg = profileImg
                    )

                    tempList.add(teacher)
                    Log.d(TAG, "TeacherList -> ${tempList}")
                }

                // set value of arraylist instance to mutable livedata.
                teacherList.value = tempList
            }
            catch (e: Exception){
                Log.d(TAG, "Parsing Error -> ${e.localizedMessage}")
            }


        },
            {
            Log.d(TAG, "Volley Error -> ${it.localizedMessage}")
        }){}

        // Livedata observer
        teacherList.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "TeacherList onStart -> ${it}")
            homeAdapter.setTeacherData(it)
        })
        queue.add(jsonArrayRequest)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}