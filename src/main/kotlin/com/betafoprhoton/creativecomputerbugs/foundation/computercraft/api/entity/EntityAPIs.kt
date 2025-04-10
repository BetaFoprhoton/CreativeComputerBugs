package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity

import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.world.entity.Entity

enum class EntityAPIs(val entity: Class<out Entity>, val api: Class<out AbstractEntityAPI>) {
    MOB(MobAPI.getSupportedClass(), MobAPI::class.java)

    ;

    companion object {
        private val ENTITY_API_REGISTRY = getTypes()

        private fun getTypes(): HashMap<Class<out Entity>, Class<out AbstractEntityAPI>> {
            val values = HashMap<Class<out Entity>, Class<out AbstractEntityAPI>>()
            EntityAPIs.entries.forEach { values[it.entity] = it.api }
            return values
        }

        fun <T> addAPI(computer: ServerComputer, entity: T): Boolean {
            var flag = false
            val entityClass = entity!!::class.java
            ENTITY_API_REGISTRY.forEach { (apiEntityClass, apiClass) ->
                if (apiEntityClass.isAssignableFrom(entityClass)) {
                    val apiInstance = apiClass.getDeclaredConstructor(Entity::class.java).newInstance(entity)
                    computer.addAPI(apiInstance)
                }
            }
            return flag
        }

        fun isAPISupported(entity: Entity): Boolean {
            //TODO: Need to determine the inheritance relationship of a reflection class.
            return true
        }
    }
}