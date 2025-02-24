package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Color
import edu.androidgroupproject.R

class HealthStatButton : StatButtonEntity() {
    override fun OnClickFunction() {
        if (PlayerInfo.Companion.Instance.GetMoney() >= Cost) {
            PlayerInfo.Companion.Instance.SetMaxHealth(PlayerInfo.Companion.Instance.GetMaxHealth() + AddValue)
            Value += AddValue
            PlayerInfo.Companion.Instance.MinusMoney(Cost)

            GameSystem.Companion.Instance.SaveEditBegin()
            GameSystem.Companion.Instance.SetFloatInSave(
                "maxhealth",
                PlayerInfo.Companion.Instance.GetMaxHealth()
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
        ): HealthStatButton {
            val result = HealthStatButton()
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