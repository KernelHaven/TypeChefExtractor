arch/arm/boot/compressed: -I $srcPath/$(obj)
arch/arm/boot/compressed/font: -Dstatic=
arch/arm/kvm: -I $srcPath/arch/arm/kvm
arch/arm/kvm/arm: -I $srcPath/.
arch/arm/kvm/mmu: -I $srcPath/.
arch/arm/mach-at91/pm: -DDEBUG
arch/arm/mach-bcm/bcm_kona_smc: -DREQUIRES_SEC
arch/arm/mach-exynos: -I $srcPath/arch/arm/mach-exynos/include -I $srcPath/arch/arm/plat-samsung/include
arch/arm/mach-mvebu: -I $srcPath/arch/arm/mach-mvebu/include -I $srcPath/arch/arm/plat-orion/include
arch/arm/mach-omap2: -I $srcPath/arch/arm/mach-omap2/include -I $srcPath/arch/arm/plat-omap/include
arch/arm/mach-s5pv210: -I $srcPath/arch/arm/mach-s5pv210/include -I $srcPath/arch/arm/plat-samsung/include
arch/arm/mach-spear: -I $srcPath/arch/arm/mach-spear/include
arch/arm/mach-vexpress: -I $srcPath/arch/arm/plat-versatile/include
arch/arm/plat-omap: -I $srcPath/arch/arm/plat-omap/include
arch/arm/plat-orion: -I $srcPath/arch/arm/plat-orion/include
arch/arm/plat-samsung: -I $srcPath/arch/arm/plat-samsung/include
arch/arm/plat-versatile: -I $srcPath/arch/arm/plat-versatile/include
arch/arm/vdso: -DDISABLE_BRANCH_PROFILING
arch/arm64/crypto/aes-glue-ce: -DUSE_V8_CRYPTO_EXTENSIONS
arch/arm64/kernel/armv8_deprecated: -I $srcPath/arch/arm64/kernel
arch/arm64/kvm: -I $srcPath/arch/arm64/kvm
arch/arm64/kvm/arm: -I $srcPath/.
arch/arm64/kvm/mmu: -I $srcPath/.
arch/avr32/mach-at32ap/pm: -DDEBUG
arch/ia64/sn/kernel: -I $srcPath/arch/ia64/sn/include
arch/ia64/sn/kernel/sn2: -I $srcPath/arch/ia64/sn/include
arch/ia64/sn/pci: -I $srcPath/arch/ia64/sn/include
arch/ia64/sn/pci/pcibr: -I $srcPath/arch/ia64/sn/include
arch/ia64/uv/kernel: -I $srcPath/arch/ia64/sn/include
arch/mips/jz4740/setup: -I $srcPath/arch/mips/jz4740/../../../scripts/dtc/libfdt
arch/mips/kernel/D: -DHAVE_AS_DSP
arch/mips/mti-malta/malta-dtshim: -I $srcPath/arch/mips/mti-malta/../../../scripts/dtc/libfdt
arch/mips/vdso: -I $srcPath/%,$(KBUILD_CFLAGS))
arch/powerpc/kvm: -I $srcPath/virt/kvm -I $srcPath/arch/powerpc/kvm
arch/powerpc/kvm/e500_mmu: -I $srcPath/.
arch/powerpc/kvm/e500_mmu_host: -I $srcPath/.
arch/powerpc/kvm/emulate: -I $srcPath/.
arch/powerpc/kvm/emulate_loadstore: -I $srcPath/.
arch/powerpc/math-emu: -I $srcPath/. -I $srcPath/include/math-emu
arch/powerpc/platforms/cell/spufs/sched: -I $srcPath/arch/powerpc/platforms/cell/spufs
arch/powerpc/platforms/pseries: -DDEBUG
arch/s390/boot: -I $srcPath/.
arch/s390/boot/compressed/misc: -I $srcPath/$(obj)
arch/s390/kvm: -I $srcPath/virt/kvm -I $srcPath/arch/s390/kvm
arch/unicore32/boot/compressed/font: -Dstatic=
arch/x86/kernel/irq: -I $srcPath/arch/x86/kernel/../include/asm/trace
arch/x86/kvm: -I $srcPath/arch/x86/kvm
arch/x86/kvm/svm: -I $srcPath/.
arch/x86/kvm/vmx: -I $srcPath/.
arch/x86/kvm/x86: -I $srcPath/.
arch/x86/mm/fault: -I $srcPath/arch/x86/mm/../include/asm/trace
arch/xtensa/boot/lib: -I $srcPath/lib/zlib_inflate
drivers/acpi: -DACPI_DEBUG_OUTPUT
drivers/acpi/acpica: -DBUILDING_ACPICA -DACPI_DEBUG_OUTPUT
drivers/android: -I $srcPath/drivers/android
drivers/base: -DDEBUG
drivers/base/power: -DDEBUG
drivers/base/power/opp: -DDEBUG
drivers/base/regmap/regmap: -I $srcPath/drivers/base/regmap
drivers/bcma: -DDEBUG
drivers/bluetooth: -D__CHECK_ENDIAN__
drivers/char/mwave: -DMW_TRACE
drivers/crypto/qat/qat_dh895xcc: -I $srcPath/drivers/crypto/qat/qat_dh895xcc/../qat_common
drivers/crypto/qat/qat_dh895xccvf: -I $srcPath/drivers/crypto/qat/qat_dh895xccvf/../qat_common
drivers/crypto/ux500/cryp/cryp: -DDEBUG
drivers/crypto/ux500/cryp/cryp_core: -DDEBUG
drivers/crypto/ux500/cryp/cryp_irq: -DDEBUG
drivers/crypto/ux500/hash/hash_core: -DDEBUG
drivers/gpio: -DDEBUG
drivers/gpu/drm/amd/amdgpu: -I $srcPath/include/drm -I $srcPath/drivers/gpu/drm/amd/include/asic_reg -I $srcPath/drivers/gpu/drm/amd/include -I $srcPath/drivers/gpu/drm/amd/amdgpu -I $srcPath/drivers/gpu/drm/amd/scheduler
drivers/gpu/drm/amd/amdgpu/amdgpu_trace_points: -I $srcPath/drivers/gpu/drm/amd/amdgpu
drivers/gpu/drm/amd/amdkfd: -I $srcPath/include/drm -I $srcPath/drivers/gpu/drm/amd/include/ -I $srcPath/drivers/gpu/drm/amd/include/asic_reg
drivers/gpu/drm/ast: -I $srcPath/include/drm
drivers/gpu/drm/bochs: -I $srcPath/include/drm
drivers/gpu/drm/bridge: -I $srcPath/include/drm
drivers/gpu/drm/cirrus: -I $srcPath/include/drm
drivers/gpu/drm/drm_trace_points: -I $srcPath/drivers/gpu/drm
drivers/gpu/drm/exynos: -I $srcPath/include/drm -I $srcPath/drivers/gpu/drm/exynos
drivers/gpu/drm/gma500: -I $srcPath/include/drm
drivers/gpu/drm/i2c: -I $srcPath/include/drm
drivers/gpu/drm/i810: -I $srcPath/include/drm
drivers/gpu/drm/i915/i915_trace_points: -I $srcPath/drivers/gpu/drm/i915
drivers/gpu/drm/mga: -I $srcPath/include/drm
drivers/gpu/drm/mgag200: -I $srcPath/include/drm
drivers/gpu/drm/msm: -I $srcPath/include/drm -I $srcPath/drivers/gpu/drm/msm -I $srcPath/drivers/gpu/drm/msm/dsi
drivers/gpu/drm/nouveau: -I $srcPath/include/drm -I $srcPath/drivers/gpu/drm/nouveau/include -I $srcPath/drivers/gpu/drm/nouveau/include/nvkm -I $srcPath/drivers/gpu/drm/nouveau/nvkm -I $srcPath/drivers/gpu/drm/nouveau
drivers/gpu/drm/omapdrm: -I $srcPath/include/drm
drivers/gpu/drm/qxl: -I $srcPath/include/drm
drivers/gpu/drm/r128: -I $srcPath/include/drm
drivers/gpu/drm/radeon: -I $srcPath/include/drm -I $srcPath/drivers/gpu/drm/amd/include
drivers/gpu/drm/radeon/radeon_trace_points: -I $srcPath/drivers/gpu/drm/radeon
drivers/gpu/drm/savage: -I $srcPath/include/drm
drivers/gpu/drm/sis: -I $srcPath/include/drm
drivers/gpu/drm/tdfx: -I $srcPath/include/drm
drivers/gpu/drm/tegra: -DDEBUG
drivers/gpu/drm/tilcdc: -I $srcPath/include/drm
drivers/gpu/drm/ttm: -I $srcPath/include/drm
drivers/gpu/drm/udl: -I $srcPath/include/drm
drivers/gpu/drm/vc4: -I $srcPath/include/drm
drivers/gpu/drm/vgem: -I $srcPath/include/drm
drivers/gpu/drm/via: -I $srcPath/include/drm
drivers/gpu/drm/virtio: -I $srcPath/include/drm
drivers/gpu/drm/vmwgfx: -I $srcPath/include/drm
drivers/hwmon: -DDEBUG
drivers/i2c: -DDEBUG
drivers/i2c/algos: -DDEBUG
drivers/i2c/busses: -DDEBUG
drivers/i2c/muxes: -DDEBUG
drivers/ide: -I $srcPath/drivers/ide
drivers/infiniband/hw/cxgb3: -I $srcPath/drivers/net/ethernet/chelsio/cxgb3 -DDEBUG
drivers/infiniband/hw/cxgb4: -I $srcPath/drivers/net/ethernet/chelsio/cxgb4
drivers/infiniband/hw/ocrdma: -I $srcPath/drivers/net/ethernet/emulex/benet
drivers/infiniband/hw/usnic: -I $srcPath/drivers/net/ethernet/cisco/enic
drivers/infiniband/ulp/isert: -I $srcPath/drivers/target -I $srcPath/drivers/target/iscsi
drivers/infiniband/ulp/srpt: -I $srcPath/drivers/target
drivers/md/bcache/request: -I $srcPath/block
drivers/media/common/b2c2: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/ -I $srcPath/drivers/media/tuners/
drivers/media/common/siano: -I $srcPath/drivers/media/dvb-core
drivers/media/dvb-frontends: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/tuners/
drivers/media/dvb-frontends/drx39xyj: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/tuners/
drivers/media/firewire: -I $srcPath/drivers/media/dvb-core
drivers/media/i2c/cx25840: -I $srcPath/drivers/media/i2c
drivers/media/i2c/smiapp: -I $srcPath/drivers/media/i2c
drivers/media/mmc/siano: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/common/siano
drivers/media/pci/b2c2: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/common/b2c2/
drivers/media/pci/bt8xx: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/common -I $srcPath/drivers/media/tuners
drivers/media/pci/cx18: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends -I $srcPath/drivers/media/tuners
drivers/media/pci/cx23885: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/pci/cx25821: -I $srcPath/drivers/media/i2c
drivers/media/pci/cx88: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/pci/ddbridge: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/ -I $srcPath/drivers/media/tuners/ -I $srcPath/drivers/staging/media/cxd2099/
drivers/media/pci/dm1105: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends
drivers/media/pci/ivtv: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/pci/mantis: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/
drivers/media/pci/netup_unidvb: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/pci/ngene: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/ -I $srcPath/drivers/media/tuners/ -I $srcPath/drivers/staging/media/cxd2099/
drivers/media/pci/pluto2: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/
drivers/media/pci/pt1: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/pci/pt3: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends -I $srcPath/drivers/media/tuners
drivers/media/pci/saa7134: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends -I $srcPath/drivers/media/usb/go7007
drivers/media/pci/saa7146: -I $srcPath/drivers/media/i2c
drivers/media/pci/saa7164: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/pci/smipcie: -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/pci/ttpci: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/ -I $srcPath/drivers/media/tuners
drivers/media/platform: -I $srcPath/drivers/media/i2c
drivers/media/platform/coda: -I $srcPath/drivers/media/platform/coda
drivers/media/platform/omap3isp: -DDEBUG
drivers/media/platform/sti/c8sectpfe: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/common -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/ -I $srcPath/drivers/media/tuners/
drivers/media/platform/ti-vpe: -DDEBUG
drivers/media/radio: -I $srcPath/sound
drivers/media/tuners: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/usb/as102: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/usb/au0828: -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/usb/b2c2: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/common/b2c2/
drivers/media/usb/cx231xx: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends -I $srcPath/drivers/media/usb/dvb-usb
drivers/media/usb/dvb-usb: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends/ -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/pci/ttpci
drivers/media/usb/dvb-usb-v2: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/common
drivers/media/usb/em28xx: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/usb/go7007: -I $srcPath/drivers/media/common
drivers/media/usb/gspca/gl860: -I $srcPath/drivers/media/usb/gspca
drivers/media/usb/gspca/m5602: -I $srcPath/drivers/media/usb/gspca
drivers/media/usb/gspca/stv06xx: -I $srcPath/drivers/media/usb/gspca
drivers/media/usb/hdpvr: -I $srcPath/drivers/media/i2c
drivers/media/usb/pvrusb2: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/usb/siano: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/common/siano
drivers/media/usb/stk1160: -I $srcPath/drivers/media/i2c
drivers/media/usb/tm6000: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends
drivers/media/usb/ttusb-budget: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends
drivers/media/usb/ttusb-dec: -I $srcPath/drivers/media/dvb-core/
drivers/media/usb/usbvision: -I $srcPath/drivers/media/i2c -I $srcPath/drivers/media/tuners
drivers/media/v4l2-core: -I $srcPath/drivers/media/dvb-core -I $srcPath/drivers/media/dvb-frontends -I $srcPath/drivers/media/tuners
drivers/misc/cb710: -DDEBUG
drivers/misc/cxl/trace: -I $srcPath/drivers/misc/cxl
drivers/misc/mei/mei-trace: -I $srcPath/drivers/misc/mei
drivers/misc/mic/card: -DINTEL_MIC_CARD
drivers/misc/sgi-gru: -DDEBUG
drivers/mtd/devices/docg3: -I $srcPath/drivers/mtd/devices
drivers/net/caif: -DDEBUG
drivers/net/ethernet/altera: -D__CHECK_ENDIAN__
drivers/net/ethernet/atheros/alx: -D__CHECK_ENDIAN__
drivers/net/ethernet/dec/tulip: -DDEBUG
drivers/net/fddi/skfp: -I $srcPath/drivers/net/skfp -DPCI -DMEM_MAPPED_IO
drivers/net/wan/lmc: -I $srcPath/.
drivers/net/wireless/ath: -D__CHECK_ENDIAN__
drivers/net/wireless/ath/ath10k/trace: -I $srcPath/drivers/net/wireless/ath/ath10k
drivers/net/wireless/ath/ath5k/base: -I $srcPath/drivers/net/wireless/ath/ath5k
drivers/net/wireless/ath/ath6kl/trace: -I $srcPath/drivers/net/wireless/ath/ath6kl
drivers/net/wireless/ath/trace: -I $srcPath/drivers/net/wireless/ath
drivers/net/wireless/ath/wil6210/trace: -I $srcPath/drivers/net/wireless/ath/wil6210
drivers/net/wireless/brcm80211/brcmfmac: -I $srcPath/drivers/net/wireless/brcm80211/brcmfmac -I $srcPath/drivers/net/wireless/brcm80211/include -D__CHECK_ENDIAN__
drivers/net/wireless/brcm80211/brcmsmac: -D__CHECK_ENDIAN__ -I $srcPath/drivers/net/wireless/brcm80211/brcmsmac -I $srcPath/drivers/net/wireless/brcm80211/brcmsmac/phy -I $srcPath/drivers/net/wireless/brcm80211/include
drivers/net/wireless/brcm80211/brcmutil: -I $srcPath/drivers/net/wireless/brcm80211/brcmutil -I $srcPath/drivers/net/wireless/brcm80211/include
drivers/net/wireless/iwlegacy: -D__CHECK_ENDIAN__
drivers/net/wireless/iwlwifi: -D__CHECK_ENDIAN__ -I $srcPath/drivers/net/wireless/iwlwifi
drivers/net/wireless/iwlwifi/dvm: -D__CHECK_ENDIAN__ -I $srcPath/drivers/net/wireless/iwlwifi/dvm/../
drivers/net/wireless/iwlwifi/iwl-devtrace: -I $srcPath/drivers/net/wireless/iwlwifi
drivers/net/wireless/iwlwifi/mvm: -D__CHECK_ENDIAN__ -I $srcPath/drivers/net/wireless/iwlwifi/mvm/../
drivers/net/wireless/mediatek/mt7601u: -D__CHECK_ENDIAN__
drivers/net/wireless/mediatek/mt7601u/trace: -I $srcPath/drivers/net/wireless/mediatek/mt7601u
drivers/net/wireless/mwifiex: -D__CHECK_ENDIAN
drivers/net/wireless/orinoco: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtl818x/rtl8180: -I $srcPath/drivers/net/wireless/realtek/rtl818x
drivers/net/wireless/realtek/rtl818x/rtl8187: -I $srcPath/drivers/net/wireless/realtek/rtl818x
drivers/net/wireless/realtek/rtlwifi: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/btcoexist: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8188ee: -I $srcPath/drivers/net/wireless/rtlwifi -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8192c: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8192ce: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8192cu: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8192de: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8192ee: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8192se: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8723ae: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8723be: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8723com: -D__CHECK_ENDIAN__
drivers/net/wireless/realtek/rtlwifi/rtl8821ae: -D__CHECK_ENDIAN__
drivers/net/wireless/ti/wl1251: -D__CHECK_ENDIAN__
drivers/net/wireless/ti/wlcore: -D__CHECK_ENDIAN__
drivers/net/wireless/zd1211rw: -DDEBUG
drivers/pci: -DDEBUG
drivers/pps: -DDEBUG
drivers/pps/clients: -DDEBUG
drivers/regulator: -DDEBUG
drivers/rtc: -DDEBUG
drivers/scsi/aacraid: -I $srcPath/drivers/scsi
drivers/scsi/aha152x: -DAHA152X_STAT -DAUTOCONF
drivers/scsi/aic7xxx: -I $srcPath/drivers/scsi
drivers/scsi/aic94xx: -DASD_DEBUG -DASD_ENTER_EXIT
drivers/scsi/csiostor: -I $srcPath/drivers/net/ethernet/chelsio/cxgb4
drivers/scsi/cxgbi/cxgb3i: -I $srcPath/drivers/net/ethernet/chelsio/cxgb3
drivers/scsi/cxgbi/cxgb4i: -I $srcPath/drivers/net/ethernet/chelsio/cxgb4
drivers/scsi/mvsas: -DMV_DEBUG
drivers/scsi/pcmcia: -I $srcPath/drivers/scsi
drivers/spi: -DDEBUG
drivers/staging/android: -I $srcPath/drivers/staging/android
drivers/staging/comedi: -DDEBUG
drivers/staging/comedi/drivers: -DDEBUG
drivers/staging/comedi/kcomedilib: -DDEBUG
drivers/staging/media/cxd2099: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/ -I $srcPath/drivers/media/tuners/
drivers/staging/media/mn88472: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/ -I $srcPath/drivers/media/tuners/
drivers/staging/media/mn88473: -I $srcPath/drivers/media/dvb-core/ -I $srcPath/drivers/media/dvb-frontends/ -I $srcPath/drivers/media/tuners/
drivers/staging/most/aim-cdev: -I $srcPath/drivers/staging/most/mostcore/
drivers/staging/most/aim-network: -I $srcPath/drivers/staging/most/mostcore/
drivers/staging/most/aim-sound: -I $srcPath/drivers/staging/most/mostcore/
drivers/staging/most/aim-v4l2: -I $srcPath/drivers/staging/most/mostcore/
drivers/staging/most/hdm-dim2: -I $srcPath/drivers/staging/most/mostcore/ -I $srcPath/drivers/staging/most/aim-network/
drivers/staging/most/hdm-i2c: -I $srcPath/drivers/staging/most/mostcore/
drivers/staging/most/hdm-usb: -I $srcPath/drivers/staging/most/mostcore/ -I $srcPath/drivers/staging/most/aim-network/
drivers/staging/rdma/amso1100: -DDEBUG
drivers/staging/rdma/hfi1/trace: -I $srcPath/drivers/staging/rdma/hfi1
drivers/staging/rdma/ipath: -DIPATH_IDSTR='"QLogic kernel.org driver"' -DIPATH_KERN_TYPE=0
drivers/staging/rtl8188eu: -D__CHECK_ENDIAN__ -I $srcPath/drivers/staging/rtl8188eu/include
drivers/staging/rtl8192e: -D__CHECK_ENDIAN__
drivers/staging/rtl8192e/rtl8192e: -D__CHECK_ENDIAN__
drivers/staging/rtl8192u: -DCONFIG_FORCE_HARD_FLOAT=y -DJACKSON_NEW_8187 -DJACKSON_NEW_RX -DTHOMAS_BEACON -DTHOMAS_TASKLET -DTHOMAS_SKB -DTHOMAS_TURBO -I $srcPath/drivers/staging/rtl8192u/ieee80211
drivers/staging/rtl8192u/ieee80211: -I $srcPath//drivers/net/wireless -DJACKSON_NEW_8187 -DJACKSON_NEW_RX
drivers/staging/rtl8723au: -D__CHECK_ENDIAN__ -I $srcPath/drivers/staging/rtl8723au/include
drivers/staging/rts5208: -I $srcPath/drivers/scsi
drivers/staging/unisys/visorbus: -I $srcPath/drivers/staging/unisys/include
drivers/staging/unisys/visorhba: -I $srcPath/drivers/staging/unisys/include
drivers/staging/unisys/visorinput: -I $srcPath/drivers/staging/unisys/include
drivers/staging/unisys/visornic: -I $srcPath/drivers/staging/unisys/include
drivers/staging/vt6655: -DLINUX -D__KERNEL__ -D__NO_VERSION__ -DHOSTAP
drivers/staging/vt6656: -DLINUX -D__KERNEL__ -DEXPORT_SYMTAB -D__NO_VERSION__ -DHOSTAP
drivers/staging/wilc1000: -DWILC_SDIO -DCOMPLEMENT_BOOT -DWILC_SDIO_IRQ_GPIO -DWILC_SPI -DSTA_FIRMWARE=\"atmel/wilc1000_fw.bin\"  		-DAP_FIRMWARE=\"\" -I $srcPath/drivers/staging/wilc1000/ -D__CHECK_ENDIAN__ -DWILC_ASIC_A0 -DWILC_DEBUGFS -DMEMORY_STATIC -DWILC_PREALLOC_AT_INSMOD -DWILC_NORMAL_ALLOC
drivers/tty/serial/8250/8250_ingenic: -I $srcPath/scripts/dtc/libfdt
drivers/usb/chipidea: -DDEBUG
drivers/usb/dwc2: -DDEBUG -DVERBOSE_DEBUG
drivers/usb/dwc3/trace: -I $srcPath/drivers/usb/dwc3
drivers/usb/gadget: -I $srcPath/drivers/usb/gadget/udc
drivers/usb/gadget/function: -I $srcPath/drivers/usb/gadget/ -I $srcPath/drivers/usb/gadget/udc/
drivers/usb/gadget/legacy: -I $srcPath/drivers/usb/gadget/ -I $srcPath/drivers/usb/gadget/udc/ -I $srcPath/drivers/usb/gadget/function/
drivers/usb/host/xhci-trace: -I $srcPath/drivers/usb/host
drivers/usb/storage: -I $srcPath/drivers/scsi
drivers/usb/usbip: -DDEBUG
drivers/usb/wusbcore: -DDEBUG
drivers/vfio/platform/reset: -I $srcPath/drivers/vfio/platform
drivers/video/fbdev/intelfb: -DDEBUG -DREGDUMP
drivers/video/fbdev/omap2/dss: -DDEBUG
fs/befs: -DDEBUG
fs/gfs2: -I $srcPath/fs/gfs2
fs/jfs: -D_JFS_4K
fs/nfs/nfs4trace: -I $srcPath/fs/nfs
fs/nfs/nfstrace: -I $srcPath/fs/nfs
fs/nfsd: -I $srcPath/fs/nfsd
fs/ntfs: -DNTFS_VERSION=\"2.1.32\" -DDEBUG -DNTFS_RW
fs/ocfs2: -I $srcPath/fs/ocfs2 -DCATCH_BH_JBD_RACES
fs/ocfs2/dlm: -I $srcPath/fs/ocfs2
fs/ocfs2/dlmfs: -I $srcPath/fs/ocfs2
fs/qnx6: -DDEBUG
fs/ufs: -DDEBUG
fs/xfs: -I $srcPath/fs/xfs -I $srcPath/fs/xfs/libxfs
kernel/power: -DDEBUG
kernel/trace/trace_benchmark: -I $srcPath/kernel/trace
kernel/trace/trace_events_filter: -I $srcPath/kernel/trace
lib/kobject: -DDEBUG
lib/kobject_uevent: -DDEBUG
net/caif: -DDEBUG
net/ieee802154: -D__CHECK_ENDIAN__
net/ieee802154/trace: -I $srcPath/net/ieee802154
net/mac80211: -D__CHECK_ENDIAN__ -DDEBUG
net/mac80211/trace: -I $srcPath/net/mac80211
net/mac802154: -D__CHECK_ENDIAN__
net/mac802154/trace: -I $srcPath/net/mac802154
net/rds: -DDEBUG
net/wireless: -D__CHECK_ENDIAN__
net/wireless/trace: -I $srcPath/net/wireless
samples/trace_events/trace-events-sample: -I $srcPath/samples/trace_events
security/selinux: -I $srcPath/security/selinux -I $srcPath/security/selinux/include
sound/core/pcm_lib: -I $srcPath/sound/core
sound/hda/trace: -I $srcPath/sound/hda
sound/pci/hda/hda_controller: -I $srcPath/sound/pci/hda
sound/pci/hda/hda_intel: -I $srcPath/sound/pci/hda
tools/testing/nvdimm/test: -I $srcPath/tools/testing/nvdimm/test/../../../../drivers/nvdimm/ -I $srcPath/tools/testing/nvdimm/test/../../../../drivers/acpi/
