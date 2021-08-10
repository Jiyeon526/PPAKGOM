<template>
  <div v-if="open">
    머가 나오니??
    <slot />
  </div>
</template>

<script>
function copyStyles(sourceDoc, targetDoc) {
  Array.from(sourceDoc.styleSheets).forEach(styleSheet => {
    if (styleSheet.cssRules) {
      const newStyleEl = sourceDoc.createElement("style");

      Array.from(styleSheet.cssRules).forEach(cssRule => {
        newStyleEl.appendChild(sourceDoc.createTextNode(cssRule.cssText));
      });

      targetDoc.head.appendChild(newStyleEl);
    } else if (styleSheet.href) {
      const newLinkEl = sourceDoc.createElement("link");

      newLinkEl.rel = "stylesheet";
      newLinkEl.href = styleSheet.href;
      targetDoc.head.appendChild(newLinkEl);
    }
  });
}

export default {
  model: {
    prop: "open",
    event: "close"
  },
  props: {
    open: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      windowRef: null
    };
  },
  watch: {
    open(newOpen) {
      if (newOpen) {
        this.openPopup();
      } else {
        this.closePopup();
      }
    }
  },
  methods: {
    openPopup() {
      this.windowRef = window.open(
        "",
        "",
        "width=600,height=400,left=200,top=200"
      );
      this.windowRef.document.body.appendChild(this.$el);
      copyStyles(window.document, this.windowRef.document);
      this.windowRef.addEventListener("beforeunload", this.closePopup);
    },
    closePopup() {
      if (this.windowRef) {
        this.windowRef.close();
        this.windowRef.removeEventListener("beforeunload", this.closePopup);
        this.windowRef = null;
        this.$emit("close", false); // 부모창의 binding된 open에게 값을 넘김
      }
    }
  },
  mounted() {
    if (this.open) {
      this.openPopup();
    }
  },
  beforeDestroy() {
    if (this.windowRef) {
      this.closePopup();
    }
  }
};
</script>
