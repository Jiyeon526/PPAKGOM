<template>
  <el-container
    class="main-wrapper"
    v-loading="loading"
    element-loading-text="Loading..."
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(255, 255, 255, 0.5)"
  >
    <main-header
      :height="`70px`"
      @openLoginDialog="onOpenLoginDialog"
      @openRegisterDialog="onOpenRegisterDialog"
      @openRoomDialog="onOpenRoomDialog"
      @openAnswerworkbookDialog="onOpenAnswerWorkbookDialog"
      @openInviteDialog="onOpenInviteDialog"
      @openMakeworkbookDialog="onOpenMakeworkbookDialog"
      @openOtherpeopleDialog="onOpenOtherpeopleDialog"
    />
    <el-container class="main-container">
      <!-- <el-aside class="hide-on-small" > -->
      <main-sidebar />
      <!-- </el-aside> -->
      <el-main>
        <router-view
          @openStudyscheduleDialog="onOpenStudyscheduleDialog"
          @openStudydetailDialog="onOpenStudydetailDialog"
          @openAnswerworkbookDialog="onOpenAnswerWorkbookDialog"
          @openMakeworkbookDialog="onOpenMakeworkbookDialog"
          @openOtherpeopleDialog="onOpenOtherpeopleDialog"
        ></router-view>
      </el-main>
    </el-container>
    <main-footer :height="`50px`" />
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
    :userData="profileData"
    @closeOtherpeopleDialog="onCloseOtherpeopleDialog"
  />
  <studydetail-dialog
    :open="studydetailDialogOpen"
    :selectStudy="selectStudyDetail"
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
      selectStudyDetail: [],
      studyscheduleDialogOpen: false,
      profileData: []
      // selectScheduleDate: null
    };
  },
  methods: {
    onOpenStudyscheduleDialog(selectDate) {
      this.studyscheduleDialogOpen = true;
      // this.selectScheduleDate=selectDate
    },
    onCloseStudyscheduleDialog() {
      this.studyscheduleDialogOpen = false;
    },

    onOpenStudydetailDialog(selectStudy) {
      this.studydetailDialogOpen = true;
      this.selectStudyDetail = selectStudy;
    },
    onCloseStudydetailDialog() {
      this.studydetailDialogOpen = false;
    },

    onOpenOtherpeopleDialog(userProfile) {
      this.otherpeopleDialogOpen = true;
      this.profileData = userProfile
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
