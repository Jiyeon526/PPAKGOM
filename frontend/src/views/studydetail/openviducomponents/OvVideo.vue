<template>
  <video
    autoplay
    :class="{ videoborder: isSpeak, videobaseborder: !isSpeak }"
  />
</template>

<script>
export default {
  name: "OvVideo",

  props: {
    streamManager: Object,
    isSpeak: Boolean
  },
  watch: {
    streamManager() {
      this.streamManager.addVideoElement(this.$el);
    }
  },
  mounted() {
    this.streamManager.addVideoElement(this.$el);
  }
};
</script>
<style>
.videoborder {
  border: 4px solid #ff006a;
  border-radius: 15px;
  margin: 0px;
  padding: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}

.videobaseborder {
  border: 4px solid #000000;
  border-radius: 15px;
  margin: 0px;
  padding: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}
</style>
