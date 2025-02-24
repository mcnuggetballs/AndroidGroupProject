package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Color

class DamageStatButton : StatButtonEntity() {
    override fun OnClickFunction() {
        if (PlayerInfo.Companion.Instance.GetMoney() >= Cost) {
            PlayerInfo.Companion.Instance.GetMainWeapon()!!.SetWeaponDamage(
                PlayerInfo.Companion.Instance.GetMainWeapon()!!.GetWeaponDamage() + AddValue
            )
            Value += AddValue
            PlayerInfo.Companion.Instance.MinusMoney(Cost)
            GameSystem.Companion.Instance.SaveEditBegin()
            GameSystem.Companion.Instance.SetIntInSave(
                "maingunshootdamage", PlayerInfo.Companion.Instance.GetMainWeapon()!!
                    .GetWeaponDamage()
            )
            GameSystem.Companion.Instance.SetIntInSave(
                "money",
                PlayerInfo.Companion.Instance.GetMoney()
            )
            GameSystem.Companion.Instance.SaveEditEnd()
        }
    }

    override fun OffClickFunction() {
    }

    companion object {
        fun Create(
            xPos: Float,
            yPos: Float,
            value: Int,
            addvalue: Int,
            cost: Int,
            buttonSize: Float,
            textSize: Float
        ): DamageStatButton {
            val result = DamageStatButton()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.bmp = Bitmap.createScaledBitmap(
                ResourceManager.Companion.Instance.GetBitmap(R.drawable.plusbutton)!!,
                buttonSize.toInt(),
                buttonSize.toInt(),
                true
            )
            result.coinBitMap = Bitmap.createScaledBitmap(
                ResourceManager.Companion.Instance.GetBitmap(R.drawable.coin)!!,
                buttonSize.toInt(),
                buttonSize.toInt(),
                true
            )
            result.Value = value
            result.AddValue = addvalue
            result.Cost = cost
            result.paint.color = Color.BLACK
            result.paint.textSize = textSize
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}