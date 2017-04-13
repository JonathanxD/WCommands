/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.github.jonathanxd.wcommands.reflection;

import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import org.junit.Test;

public class BasicReflectionTest {

    @Test
    public void reflectionTest() {
        ReflectionCommandProcessor processor = ReflectionAPI.createWCommand();

        processor.getRegister(RegistrationTicket.empty(this)).addCommandFromClass(Gun.class, aClass -> {
            try {
                return aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        processor.processAndInvoke("gun", "ammo", "40", "cartridges", "20");
        processor.processAndInvoke("gun", "shoot");

    }

    @Command(name = "gun")
    public static final class Gun {

        private int ammo = 20;
        private int cartridges = 10;

        private int currentAmmo = this.ammo;
        private int currentCartridges = this.cartridges;

        @Command
        public void shoot() {
            if (this.currentAmmo == 0) {
                System.out.println("No ammo.");
                return;
            }

            this.currentAmmo--;

            System.out.println("Shoot. " + this.currentAmmo + "/" + this.currentCartridges);

            if (this.currentAmmo == 0 && this.currentCartridges > 0) {
                this.currentAmmo = this.ammo;
                this.currentCartridges--;
            }

        }

        public int getAmmo() {
            return this.ammo;
        }

        @Command(name = "ammo")
        public void setAmmo(@Argument(id = "ammo") int ammo) {
            this.currentAmmo = ammo;
            this.ammo = ammo;
        }

        public int getCartridges() {
            return this.cartridges;
        }

        @Command(name = "cartridges")
        public void setCartridges(@Argument(id = "cartridges") int cartridges) {
            this.currentCartridges = cartridges;
            this.cartridges = cartridges;
        }
    }


}
