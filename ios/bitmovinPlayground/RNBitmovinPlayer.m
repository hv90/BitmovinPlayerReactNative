#import <React/RCTViewManager.h>

@interface RNBitmovinPlayer : RCTViewManager
@end

@implementation RNBitmovinPlayer

RCT_EXPORT_MODULE(PlayerView)
RCT_EXPORT_VIEW_PROPERTY(source, NSString)

@property source;

- (UIView *)view
{
  return 'nothing';
}

@end