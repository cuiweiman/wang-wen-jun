package com.cui.behavior.mediator;

/**
 * @description: 抽象中介者
 * @author: wei·man cui
 * @date: 2020/7/16
 */
public abstract class Mediator {
    public abstract void componentChanged(Component c);
}

/**
 * @description: 具体中介者 维持对各个同事对象的引用
 * @author: wei·man cui
 * @date: 2020/7/16
 */
class ConcreteMediator extends Mediator {
    public Button addButton;
    public List list;
    public TextBox userNameTextBox;
    public ComboBox cb;

    /**
     * 封装同事对象之间的交互
     *
     * @param c 参数
     */
    @Override
    public void componentChanged(Component c) {
        //单击按钮
        if (c == addButton) {
            System.out.println("--单击增加按钮--");
            list.update();
            cb.update();
            userNameTextBox.update();
        }
        //从列表框选择客户
        else if (c == list) {
            System.out.println("--从列表框选择客户--");
            cb.select();
            userNameTextBox.setText();
        }
        //从组合框选择客户
        else if (c == cb) {
            System.out.println("--从组合框选择客户--");
            cb.select();
            userNameTextBox.setText();
        }
    }
}

/**
 * @description: 抽象组件类：抽象同事类
 * @author: wei·man cui
 * @date: 2020/7/16
 */
abstract class Component {
    protected Mediator mediator;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    //转发调用
    public void changed() {
        mediator.componentChanged(this);
    }

    public abstract void update();
}

/**
 * @description: 按钮类：具体同事类
 * @author: wei·man cui
 * @date: 2020/7/16
 */
class Button extends Component {
    @Override
    public void update() {
        //按钮不产生交互
    }
}

/**
 * @description: 列表框类：具体同事类
 * @author: wei·man cui
 * @date: 2020/7/16
 */
class List extends Component {
    @Override
    public void update() {
        System.out.println("列表框增加一项：张无忌。");
    }

    public void select() {
        System.out.println("列表框选中项：小龙女。");
    }
}

/**
 * @description: 组合框类：具体同事类
 * @author: wei·man cui
 * @date: 2020/7/16
 */
class ComboBox extends Component {
    @Override
    public void update() {
        System.out.println("组合框增加一项：张无忌。");
    }

    public void select() {
        System.out.println("组合框选中项：小龙女。");
    }
}

/**
 * @description: 文本框类：具体同事类
 * @author: wei·man cui
 * @date: 2020/7/16
 */
class TextBox extends Component {
    @Override
    public void update() {
        System.out.println("客户信息增加成功后文本框清空。");
    }

    public void setText() {
        System.out.println("文本框显示：小龙女。");
    }
}
