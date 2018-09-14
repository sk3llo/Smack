package com.project.coolz.smack.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.project.coolz.smack.R
import com.project.coolz.smack.models.StyleChangerModel
import com.project.coolz.smack.utils.RealmUtil
import io.realm.RealmChangeListener
import net.cachapa.expandablelayout.ExpandableLayout
import org.jetbrains.anko.support.v4.toast

open class ChangeStyleFragment:Fragment(), View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
    var expandButton: Button? = null
    var purpleStyle: ImageButton? = null
    var blueStyle: ImageButton? = null
    var greenStyle: ImageButton? = null
    var expandableLayout: ExpandableLayout? = null
            override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.horizontal_fragment, container, false)
        expandableLayout = rootView.findViewById(R.id.expandable_layout)
        expandButton =  rootView.findViewById(R.id.expand_button)
        purpleStyle = rootView.findViewById(R.id.purple_style)
        blueStyle = rootView.findViewById(R.id.blueStyle)
        greenStyle = rootView.findViewById(R.id.greenStyle)
        expandableLayout?.setOnExpansionUpdateListener(this@ChangeStyleFragment)
        expandableLayout?.setOnClickListener(this@ChangeStyleFragment)
        expandButton?.setOnClickListener(this@ChangeStyleFragment)
        purpleStyle?.setOnClickListener(this@ChangeStyleFragment)
        blueStyle?.setOnClickListener(this@ChangeStyleFragment)
        greenStyle?.setOnClickListener(this@ChangeStyleFragment)
        return rootView
    }

    override fun onExpansionUpdate(expansionFraction:Float, state:Int) {
        expandButton?.rotation = expansionFraction * 180
    }
    override fun onClick(v:View) {
        when (v.id){
            R.id.expandable_layout, R.id.expand_button -> expandableLayout?.toggle()
            R.id.purple_style -> {
                if (RealmUtil().getStyle() != 1 || RealmUtil().getStyle() == null){
                    RealmUtil().saveStyle(1)
                    expandableLayout?.toggle()
                } else{
                    expandableLayout?.toggle()
                }
            }
            R.id.blueStyle -> {
                if (RealmUtil().getStyle() != 2 || RealmUtil().getStyle() == null){
                    RealmUtil().saveStyle(2)
                    expandableLayout?.toggle()
                } else{
                    expandableLayout?.toggle()
                }
            }
            R.id.greenStyle -> {
                if (RealmUtil().getStyle() != 3 || RealmUtil().getStyle() == null){
                    RealmUtil().saveStyle(3)
                    expandableLayout?.toggle()
                } else{
                    expandableLayout?.toggle()
                }
            }
        }
    }
}