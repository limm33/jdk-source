/*
 * 文件名：ExtendsANDCombination.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： ExtendsANDCombination.java
 * 修改人：yunhai
 * 修改时间：2015年12月7日
 * 修改内容：新增
 */
package other;

import org.junit.Test;

/**
 * 继承与组合实现代码复用。其系统开销不会有本质的差别，组合只是需要多一个引用变量来引用被嵌入的对象。
 * 
 * 适用：继承（将抽象的类改造成适用于特定需求的类，如bird和Animal）；组合（有明确的整体部分的关系）。
 * 
 * 继承【是（is）的关系】；组合【有（has）的关系】
 * 
 * @author yunhai
 */
public class ExtendsANDCombination {
    class Animal {
        private void beat() {
            System.out.println("HeartBeatIng……");
        }

        public void breath() {
            beat();
            System.out.println("BreathIng……");
        }
    }

    class Birds extends Animal {
        public void fly() {
            System.out.println("I'm flying……");
        }
    }

    @Test
    public void TestExtends() {
        Birds birds = new Birds();
        birds.breath();
        birds.fly();
    }

    @Test
    public void TestCombination() {
        Animal animal = new Animal();
        Bird2 birds = new Bird2(animal);
        birds.breath();
        birds.fly();
    }

    class Bird2 {
        private Animal animal;

        public Bird2(Animal animal) {
            this.animal = animal;
        }

        public void breath() {
            animal.breath();
        }

        public void fly() {
            System.out.println("I'm flying!!!!!!!");
        }
    }
}
