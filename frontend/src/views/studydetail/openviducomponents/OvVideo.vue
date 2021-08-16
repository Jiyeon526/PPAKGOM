<template>
  <video autoplay />
</template>

<script>
export default {
  name: "OvVideo",

  props: {
    streamManager: Object
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
