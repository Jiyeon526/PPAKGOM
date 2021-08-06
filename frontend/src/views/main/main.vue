<template>
  <el-container class="main-wrapper" v-loading="loading">
    <main-header
      :height="`70px`"
      @openLoginDialog="onOpenLoginDialog"
      @openRegisterDialog="onOpenRegisterDialog"
      @openRoomDialog="onOpenRoomDialog"
      @openAnswerworkbookDialog="onOpenAnswerWorkbookDialog"
      @openInviteDialog="onOpenInviteDialog"
      @openMakeworkbookDialog="onOpenMakeworkbookDialog"
      @openOtherpeopleDialog="onOpenOtherpeopleDialog"
      @openStudydetailDialog="onOpenStudydetailDialog"
      @openStudyscheduleDialog="onOpenStudyscheduleDialog"
    />
    <el-container class="main-container">
      <el-aside class="hide-on-small" width="240px">
        <main-sidebar :width="`240px`" />
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
    <main-footer :height="`80px`" />
  </el-container>
  <login-dialog
    :open="loginDialogOpen"
    @closeLoginDialog="onCloseLoginDialog"
    @openRegisterDialog="onOpenRegisterDialog"
  />
  <register-dialog
    :open="registerDialogOpen"
    @closeRegisterDialog="onCloseRegisterDialog"
  />
  <answerworkbook-dialog
    :open="answerWorkbookDialogOpen"
    @closeAnswerWorkbookDialog="onCloseAnswerWorkbookDialog"
  />

  <invite-dialog
    :open="inviteDialogOpen"
    @closeInviteDialog="onCloseInviteDialog"
  />
  <makeworkbook-dialog
    :open="makeworkbookDialogOpen"
    @closeMakeworkbookDialog="onCloseMakeworkbookDialog"
  />
  <otherpeople-dialog
    :open="otherpeopleDialogOpen"
    @closeOtherpeopleDialg="onCloseOtherpeopleDialog"
  />
  <studydetail-dialog
    :open="studydetailDialogOpen"
    @closeStudydetailDialog="onCloseStudydetailDialog"
  />
  <studyschedule-dialog
    :open="studyscheduleDialogOpen"
    @closeStudyscheduleDialog="onCloseStudyscheduleDialog"
  />
  <room-dialog :open="roomDialogOpen" @closeRoomDialog="onCloseRoomDialog" />
</template>
<style>
@import "https://unpkg.com/element-plus/lib/theme-chalk/index.css";
@import "./main.css";
@import "../../common/css/common.css";
@import "../../common/css/element-plus.css";
</style>
<script>
import LoginDialog from "./components/login-dialog";
import MainHeader from "./components/main-header";
import MainSidebar from "./components/main-sidebar";
import MainFooter from "./components/main-footer";
import RegisterDialog from "./components/register-dialog";
import RoomDialog from "./components/room-dialog";
import InviteDialog from "./components/invite-dialog";
import AnswerworkbookDialog from "./components/answerworkbook-dialog";
import MakeworkbookDialog from "./components/makeworkbook-dialog";
import OtherpeopleDialog from "./components/otherpeople-dialog";
import StudydetailDialog from "./components/studydetail-dialog";
import StudyscheduleDialog from "./components/studyschedule-dialog";

// 컴포넌트 추가

export default {
  name: "Main",
  components: {
    MainHeader,
    MainSidebar,
    MainFooter,
    LoginDialog,
    RegisterDialog,
    RoomDialog,
    InviteDialog,
    AnswerworkbookDialog,
    MakeworkbookDialog,
    OtherpeopleDialog,
    StudydetailDialog,
    StudyscheduleDialog
  },
  data() {
    return {
      loginDialogOpen: false,
      registerDialogOpen: false,
      roomDialogOpen: false,
      answerWorkbookDialogOpen: false,
      inviteDialogOpen: false,
      loading: false,
      makeworkbookDialogOpen: false,
      otherpeopleDialogOpen: false,
      studydetailDialogOpen: false,
      studyscheduleDialogOpen: false
    };
  },
  methods: {
    onOpenStudyscheduleDialog() {
      this.studyscheduleDialogOpen = true;
    },
    onCloseStudyscheduleDialog() {
      this.studyscheduleDialogOpen = false;
    },

    onOpenStudydetailDialog() {
      this.studydetailDialogOpen = true;
    },
    onCloseStudydetailDialog() {
      this.studydetailDialogOpen = false;
    },

    onOpenOtherpeopleDialog() {
      this.otherpeopleDialogOpen = true;
    },
    onCloseOtherpeopleDialog() {
      this.otherpeopleDialogOpen = false;
    },

    onOpenMakeworkbookDialog() {
      this.makeworkbookDialogOpen = true;
    },
    onCloseMakeworkbookDialog() {
      this.makeworkbookDialogOpen = false;
    },

    onOpenAnswerWorkbookDialog() {
      this.answerWorkbookDialogOpen = true;
    },
    onCloseAnswerWorkbookDialog() {
      this.answerWorkbookDialogOpen = false;
    },
    onOpenInviteDialog() {
      this.inviteDialogOpen = true;
    },
    onCloseInviteDialog() {
      this.inviteDialogOpen = false;
    },
    onOpenLoginDialog() {
      this.loginDialogOpen = true;
    },
    onCloseLoginDialog() {
      this.loginDialogOpen = false;
    },
    onOpenRegisterDialog() {
      this.registerDialogOpen = true;
    },
    onCloseRegisterDialog() {
      this.registerDialogOpen = false;
    },
    onOpenRoomDialog() {
      this.roomDialogOpen = true;
    },
    onCloseRoomDialog() {
      this.roomDialogOpen = false;
    },
    onLoading() {
      this.loading = true;
      setTimeout(() => {
        this.loading = false;
      }, 500);
    }
  },
  mounted() {
    this.onLoading();
  },
  watch: {
    $route: "onLoading"
  }
};
</script>
