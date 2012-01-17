
<script src="http://widgets.twimg.com/j/2/widget.js"></script>

<div style="margin-left: 20%;margin-right: 20%">

<script>
new TWTR.Widget({
  version: 2,
  type: 'profile',
  rpp: 5,
  interval: 30000,
  width: 500,
  height: 300,
  theme: {
    shell: {
      background: '#cccccc',
      color: '#8a828a'
    },
    tweets: {
      background: '#fafafa',
      color: '#474347',
      links: '#1d1f1d'
    }
  },
  features: {
    scrollbar: true,
    loop: false,
    live: true,
    behavior: 'all'
  }
}).render().setUser('tgrall').start();
</script>

</div>
