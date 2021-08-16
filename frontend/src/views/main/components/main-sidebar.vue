<template>
  <el-row class="main-sidebar" :gutter="10" :style="{ width: state.width }">
    <div class="hide-on-small">
      <el-menu
        :collapse="state.isCollapse"
        :default-active="String(state.activeIndex)"
        active-text-color="#ffd04b"
        class="el-menu-vertical-demo"
        @select="menuSelect"
      >
        <div v-if="!state.isCollapse">
          <el-menu-item
            style="display: flex; justify-content:flex-end; align-items:center"
            @click="onCollapse"
            ><i class="el-icon-d-arrow-left"></i
          ></el-menu-item>
        </div>
        <div v-else>
          <el-menu-item
            style="display: flex; justify-content:flex-end; align-items:center"
            @click="onCollapse"
            ><i class="el-icon-d-arrow-right"></i
          ></el-menu-item>
        </div>
        <el-menu-item
          v-for="(item, index) in state.menuItems"
          :key="index"
          :index="index.toString()"
        >
          <i v-if="item.icon" :class="['ic', item.icon]" />
          <span>{{ item.title }}</span>
        </el-menu-item>
        <el-submenu index="1">
          <template #title>
            <i class="el-icon-location"></i>
            <span>Mypage</span>
          </template>
          <el-menu-item-group title="">
            <el-menu-item index="3"
              ><i class="el-icon-reading"></i>가입한 스터디</el-menu-item
            >
            <el-menu-item index="4"
              ><i class="el-icon-star-on"></i>찜한 스터디</el-menu-item
            >
            <el-menu-item index="5"
              ><i class="el-icon-s-management"></i>가입/초대</el-menu-item
            >
            <el-menu-item index="6"
              ><i class="el-icon-s-promotion"></i>스터디원 평가</el-menu-item
            >
            <el-menu-item>
              <el-badge class="item" :value="state.length" :max="99"
                ><i class="el-icon-s-promotion"></i> </el-badge
              >테스트 중</el-menu-item
            >
          </el-menu-item-group>
        </el-submenu>
        <el-menu-item>
          <el-badge class="item" :value="state.length" :max="99"
            ><i class="el-icon-s-promotion"></i>
          </el-badge>
          <span>테스트 중</span>
        </el-menu-item>
        <el-container style="height:270px;">
          <el-main></el-main>
          <el-footer>
            <el-popover
              placement="top"
              :width="300"
              trigger="click"
              :visible="state.visible"
            >
              <template #reference>
                <div style="margin:5px; padding:5px;">
                  <el-badge class="item" :value="state.length" :max="99">
                    <el-button
                      icon="el-icon-chat-round"
                      type="success"
                      round
                      @click="reversea"
                    >
                    </el-button>
                  </el-badge>
                </div>
              </template>
              <div style="height:550px;">보여?</div>
            </el-popover>
          </el-footer>
        </el-container>
      </el-menu>
    </div>
  </el-row>
</template>
<style>
.main-sidebar .el-menu {
  margin-top: 0;
  padding-left: 0;
}
.main-sidebar .hide-on-small {
  height: 100%;
}
.main-sidebar .hide-on-small .el-menu {
  height: 100%;
}
.main-sidebar .el-menu .el-menu-item {
  cursor: pointer;
  border-right: none;
}
.main-sidebar .el-menu .el-menu-item .ic {
  margin-right: 5px;
}
</style>
<script>
import { reactive, computed } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

export default {
  name: "main-header",

  // props: {
  //   width: {
  //     type: String,
  //     default: "180px"
  //   }
  // },
  setup() {
    const store = useStore();
    const router = useRouter();

    const state = reactive({
      isLoggedIn: computed(() => store.getters["root/isLoggedIn"]),
      searchValue: null,
      menuItems: computed(() => {
        const MenuItems = store.getters["root/getMenus"];
        const isLoggedIn = store.getters["root/isLoggedIn"];
        let keys = Object.keys(MenuItems);
        let menuArray = [];
        if (!isLoggedIn) {
          let menuObject = {};
          menuObject.icon = MenuItems["main"].icon;
          menuObject.title = MenuItems["main"].name;
          menuArray.push(menuObject);
          menuObject = {};
          menuObject.icon = MenuItems["home"].icon;
          menuObject.title = MenuItems["home"].name;
          menuArray.push(menuObject);
          return menuArray;
        }
        for (let i = 0; i < keys.length - 4; ++i) {
          let menuObject = {};
          menuObject.icon = MenuItems[keys[i]].icon;
          menuObject.title = MenuItems[keys[i]].name;
          menuArray.push(menuObject);
        }
        return menuArray;
      }),
      activeIndex: computed(() => store.getters["root/getActiveMenuIndex"]),
      isCollapse: false,
      width: "200px",
      length: 0,
      visible: false
    });

    if (state.activeIndex === -1) {
      state.activeIndex = 0;
      store.commit("root/setMenuActive", 0);
    }

    const menuSelect = function(param) {
      console.log("param", param);
      store.commit("root/setMenuActive", param);
      const MenuItems = store.getters["root/getMenus"];
      let keys = Object.keys(MenuItems);
      console.log(keys[param]);
      router.push({
        name: keys[param]
      });
    };

    const clickLogout = () => {
      store.dispatch("root/requestLogout");
      store.commit("root/deleteToken");
      router.push({
        name: "home"
      });
    };

    const onCollapse = function() {
      state.isCollapse = !state.isCollapse;
      if (state.isCollapse) {
        state.width = "70px";
      } else {
        state.width = "200px";
      }
    };
    const reversea = function() {
      state.visible = !state.visible;
    };
    return { state, menuSelect, clickLogout, onCollapse, reversea };
  }
};
</script>
