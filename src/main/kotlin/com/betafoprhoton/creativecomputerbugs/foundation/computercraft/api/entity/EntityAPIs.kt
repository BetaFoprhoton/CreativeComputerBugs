package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity

import com.betafoprhoton.creativecomputerbugs.CCBMain
import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.world.entity.Entity
import java.util.HashMap


enum class EntityAPIs(val entity: Class<out Entity>, val api: Class<out AbstractEntityAPI>) {
    MOB(MobAPI.getSupportedClass(), MobAPI::class.java)

    ;

    companion object {
        fun getTypes(): HashMap<Class<out Entity>, Class<out AbstractEntityAPI>> {
            val values = HashMap<Class<out Entity>, Class<out AbstractEntityAPI>>()
            EntityAPIs.entries.forEach { values[it.entity] = it.api }
            return values
        }

        fun <T> addAPI(computer: ServerComputer, entity: T): Boolean {
            var flag = false
            CCBMain.ENTITY_API_REGISTRY.forEach { (entityClass, apiClass) ->
                entity!!::class.java.classes.forEach {
                      if (it == entityClass) {
                          val api = apiClass.getDeclaredConstructor(Entity::class.java).newInstance(entity) ?: return false
                          computer.addAPI(api)
                          flag = true
                      }
                }
            }
            return flag
        }
    }
}