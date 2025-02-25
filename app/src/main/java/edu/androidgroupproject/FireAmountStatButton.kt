package edu.androidgroupproject

import android.graphics.Bitmap
import android.graphics.Color
import com.edu.androidgroupproject.GameSystem

class FireAmountStatButton : StatButtonEntity() {
    override fun OnClickFunction() {
        if (PlayerInfo.Instance.GetMoney() >= Cost) {
            PlayerInfo.Instance.GetMainWeapon()!!.SetShootAmount(
                PlayerInfo.Instance.GetMainWeapon()!!.GetShootAmount() + AddValue
            )
            Value += AddValue
            PlayerInfo.Instance.MinusMoney(Cost)

            GameSystem.Instance.SaveEditBegin()
            GameSystem.Instance.SetIntInSave(
                "maingunshootcount", PlayerInfo.Instance.GetMainWeapon()!!
                    .GetShootAmount()
            )
            GameSystem.Instance.SetIntInSave(
                "money",
                PlayerInfo.Instance.GetMoney()
            )
            GameSystem.Instance.SaveEditEnd()
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
        ): FireAmountStatButton {
            val result = FireAmountStatButton()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.bmp = Bitmap.createScaledBitmap(
                ResourceManager.Instance.GetBitmap(R.drawable.plusbutton)!!,
                buttonSize.toInt(),
                buttonSize.toInt(),
                true
            )
            result.coinBitMap = Bitmap.createScaledBitmap(
                ResourceManager.Instance.GetBitmap(R.drawable.coin)!!,
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